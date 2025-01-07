package com.sd.android.kreedz.feature.chat.screen.chatbox

import android.content.Context
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
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
import com.sd.lib.coroutines.FLoader
import com.sd.lib.coroutines.tryLoad
import com.sd.lib.paging.DefaultPagingDataHandler
import com.sd.lib.paging.FPaging
import com.sd.lib.paging.LoadParams
import com.sd.lib.paging.Paging
import com.sd.lib.paging.modifier
import com.sd.lib.paging.replaceFirst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChatBoxVM : BaseViewModel<ChatBoxVM.State, Any>(State()) {
  private val _chatBoxRepository = ChatBoxRepository()
  private val _onlineRepository = OnlineRepository()
  private val _accountRepository = AccountRepository()

  private val _sendLoader = FLoader()
  private val _deleteLoader = FLoader()

  private val _messagePaging: FPaging<ChatBoxItemModel> = FPaging(
    refreshKey = 0,
    pagingSource = ChatBoxMessagePagingSource(),
    pagingDataHandler = ChatBoxMessagePagingDataHandler(),
  )
  val messagePaging: Paging<ChatBoxItemModel> = _messagePaging

  val inputState = TextFieldState()

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
          _messagePaging.modifier().replaceFirst(model, model.copy(message = "<deleted>"))
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
    _messagePaging.refresh()
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

private class ChatBoxMessagePagingDataHandler : DefaultPagingDataHandler<Int, ChatBoxItemModel>() {

  override suspend fun handleRefreshData(
    totalData: List<ChatBoxItemModel>,
    params: LoadParams.Refresh<Int>,
    pageData: List<ChatBoxItemModel>,
  ): List<ChatBoxItemModel> {
    if (pageData.isEmpty()) return pageData
    return withContext(Dispatchers.IO) {
      mutableListOf<ChatBoxItemModel>()
        .apply { insertDateModel(pageData = pageData, previous = null) }
        .toList()
    }
  }

  override suspend fun handleAppendData(
    totalData: List<ChatBoxItemModel>,
    params: LoadParams.Append<Int>,
    pageData: List<ChatBoxItemModel>,
  ): List<ChatBoxItemModel> {
    if (pageData.isEmpty()) return totalData
    return withContext(Dispatchers.IO) {
      mutableListOf<ChatBoxItemModel>().apply {
        addAll(totalData)
        insertDateModel(pageData = pageData, previous = totalData.lastOrNull())
      }.toList()
    }
  }

  private fun MutableList<ChatBoxItemModel>.insertDateModel(
    pageData: List<ChatBoxItemModel>,
    previous: ChatBoxItemModel?,
  ) {
    require(pageData.isNotEmpty())
    if (previous != null) check(previous is ChatBoxMessageModel)

    val first = pageData.first().also { check(it is ChatBoxMessageModel) }
    if (previous == null || previous.dateStr != first.dateStr) {
      if (first.dateStr.isNotEmpty()) {
        add(ChatBoxDateModel(first.dateStr))
      }
    }

    if (pageData.size == 1) {
      add(first)
      return
    }

    pageData.zipWithNext { item, next ->
      check(item is ChatBoxMessageModel)
      check(next is ChatBoxMessageModel)

      add(item)

      if (item.dateStr != next.dateStr) {
        if (next.dateStr.isNotEmpty()) {
          add(ChatBoxDateModel(next.dateStr))
        }
      }

      if (next === pageData.lastOrNull()) {
        add(next)
      }
    }
  }
}