package com.sd.android.kreedz.feature.chat.screen.chatbox

import android.content.Context
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.insertSeparators
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.data.model.ChatBoxDateModel
import com.sd.android.kreedz.data.model.ChatBoxItemModel
import com.sd.android.kreedz.data.model.ChatBoxMessageModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.data.repository.AccountRepository
import com.sd.android.kreedz.data.repository.ChatBoxRepository
import com.sd.android.kreedz.data.repository.OnlineRepository
import com.sd.lib.compose.input.fSetMaxLength
import com.sd.lib.compose.paging.fPagerFlow
import com.sd.lib.compose.paging.modifier
import com.sd.lib.coroutines.FLoader
import com.sd.lib.coroutines.tryLoad
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ChatBoxVM : BaseViewModel<ChatBoxVM.State, Any>(State()) {
  private val _chatBoxRepository = ChatBoxRepository()
  private val _onlineRepository = OnlineRepository()
  private val _accountRepository = AccountRepository()

  private val _sendLoader = FLoader()
  private val _deleteLoader = FLoader()

  private var _messageItems: LazyPagingItems<ChatBoxItemModel>? = null

  private val _messageModifier = fPagerFlow(prefetchDistance = 5) { ChatBoxMessagePagingSource() }
    .cachedIn(viewModelScope)
    .modifier { it.id }

  private val _messageFlow: Flow<PagingData<ChatBoxItemModel>> = _messageModifier.flow
    .map { data ->
      data.insertSeparators { before: ChatBoxMessageModel?, after: ChatBoxMessageModel? ->
        if (after != null && after.dateStr != before?.dateStr && after.dateStr.isNotBlank()) {
          ChatBoxDateModel(dateStr = after.dateStr)
        } else null
      }
    }

  val inputState = TextFieldState()

  @Composable
  fun messages(): LazyPagingItems<ChatBoxItemModel> {
    return _messageFlow.collectAsLazyPagingItems().also {
      _messageItems = it
    }
  }

  fun clickAdd(context: Context) {
    vmLaunch {
      if (checkLogin(context)) {
        updateState {
          it.copy(showInput = true)
        }
      }
    }
  }

  fun closeInput() {
    updateState {
      it.copy(showInput = false)
    }
  }

  fun clickSend(context: Context) {
    vmLaunch {
      val content = inputState.text.toString()
      if (content.isEmpty()) return@vmLaunch
      if (checkLogin(context)) {
        _sendLoader.tryLoad {
          sendMessage(content)
        }.onFailure { error ->
          sendEffect(error)
        }
      }
    }
  }

  fun clickReply(context: Context, model: ChatBoxMessageModel) {
    vmLaunch {
      if (checkLogin(context)) {
        updateState {
          it.copy(showInput = true)
        }
        inputState.edit {
          append("@${model.author.nickname} ")
        }
      }
    }
  }

  fun clickDelete(context: Context, model: ChatBoxMessageModel) {
    vmLaunch {
      if (checkLogin(context)) {
        _deleteLoader.tryLoad {
          _chatBoxRepository.deleteMessage(model.id)
        }.onSuccess {
          _messageModifier.update(model.copy(message = "<deleted>"))
        }.onFailure { error ->
          sendEffect(error)
        }
      }
    }
  }

  fun cancelSend() {
    vmLaunch {
      _sendLoader.cancel()
    }
  }

  fun cancelDelete() {
    vmLaunch {
      _deleteLoader.cancel()
    }
  }

  private suspend fun sendMessage(content: String) {
    closeInput()
    _chatBoxRepository.sendMessage(content)

    inputState.clearText()
    _messageItems?.refresh()
  }

  private suspend fun checkLogin(context: Context): Boolean {
    return _accountRepository.hasLogin().also { hasLogin ->
      if (!hasLogin) {
        AppRouter.login(context)
      }
    }
  }

  init {
    vmLaunch {
      _sendLoader.loadingFlow.collect { data ->
        updateState {
          it.copy(isSending = data)
        }
      }
    }
    vmLaunch {
      _deleteLoader.loadingFlow.collect { data ->
        updateState {
          it.copy(isDeleting = data)
        }
      }
    }

    vmLaunch {
      _accountRepository.getUserAccountFlow().collect { data ->
        updateState {
          it.copy(userId = data?.id ?: "")
        }
      }
    }

    vmLaunch {
      _onlineRepository.getOnlineUsersFlow()
        .collect { data ->
          updateState {
            it.copy(
              onlineUsers = data.users,
              guestsCount = data.guestsCount,
            )
          }
        }
    }

    vmLaunch {
      stateFlow.map { it.maxInput }
        .distinctUntilChanged()
        .collectLatest {
          inputState.fSetMaxLength(it)
        }
    }
  }

  data class State(
    val userId: String = "",
    val guestsCount: Int = 0,
    val onlineUsers: List<UserWithIconsModel> = emptyList(),

    val isSending: Boolean = false,
    val isDeleting: Boolean = false,

    val showInput: Boolean = false,
    val maxInput: Int = 250,
  )
}