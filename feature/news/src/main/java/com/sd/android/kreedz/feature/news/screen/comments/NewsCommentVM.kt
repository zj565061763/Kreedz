package com.sd.android.kreedz.feature.news.screen.comments

import android.content.Context
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.data.model.NewsCommentGroupModel
import com.sd.android.kreedz.data.model.NewsCommentModel
import com.sd.android.kreedz.data.repository.AccountRepository
import com.sd.android.kreedz.data.repository.NewsRepository
import com.sd.lib.compose.input.fMaxLength
import com.sd.lib.coroutines.FLoader
import com.sd.lib.coroutines.tryLoad
import com.sd.lib.retry.ktx.fNetRetry
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class NewsCommentVM : BaseViewModel<NewsCommentVM.State, Any>(State()) {
   private val _newsRepository = NewsRepository()
   private val _accountRepository = AccountRepository()
   private val _commentsLoader = FLoader()

   private val _sendLoader = FLoader()
   private val _deleteLoader = FLoader()

   val inputState = TextFieldState()

   fun load(newsId: String) {
      updateState {
         it.copy(newsId = newsId)
      }
   }

   fun refresh() {
      val newsId = state.newsId
      if (newsId.isNotBlank()) {
         loadComments(newsId)
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
         it.copy(
            showInput = false,
            reply = null,
         )
      }
   }

   fun clickSend(context: Context) {
      vmLaunch {
         val newsId = state.newsId
         if (newsId.isBlank()) return@vmLaunch

         val content = inputState.text.toString()
         if (content.isEmpty()) return@vmLaunch

         if (checkLogin(context)) {
            _sendLoader.tryLoad {
               _newsRepository.sendComment(
                  newsId = newsId,
                  content = content,
                  replyCommentId = state.reply?.id,
               )
            }.onSuccess {
               updateState { it.copy(reply = null) }
               inputState.clearText()
               refresh()
               closeInput()
            }.onFailure { error ->
               sendEffect(error)
            }
         }
      }
   }

   fun clickReply(context: Context, comment: NewsCommentModel) {
      vmLaunch {
         if (checkLogin(context)) {
            updateState {
               it.copy(
                  showInput = true,
                  reply = comment,
               )
            }
         }
      }
   }

   fun clickDelete(context: Context, comment: NewsCommentModel) {
      vmLaunch {
         if (checkLogin(context)) {
            _deleteLoader.tryLoad {
               _newsRepository.deleteComment(comment.id)
            }.onSuccess {
               refresh()
            }.onFailure { error ->
               sendEffect(error)
            }
         }
      }
   }

   private fun loadComments(newsId: String) {
      if (newsId.isBlank()) return
      vmLaunch {
         _commentsLoader.load {
            fNetRetry {
               loadCommentsInternal(newsId)
            }.getOrThrow()
         }.onFailure { error ->
            sendEffect(error)
         }
      }
   }

   private suspend fun loadCommentsInternal(newsId: String) {
      val data = _newsRepository.comments(newsId)
      updateState {
         it.copy(
            commentCount = data.count,
            comments = data.groups
         )
      }
   }

   fun cancelSend() {
      vmLaunch {
         _sendLoader.cancelLoad()
      }
   }

   fun cancelDelete() {
      vmLaunch {
         _deleteLoader.cancelLoad()
      }
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
         _commentsLoader.loadingFlow.collect { data ->
            updateState {
               it.copy(isLoading = data)
            }
         }
      }
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
         stateFlow.map { it.newsId }
            .distinctUntilChanged()
            .onEach {
               updateState {
                  it.copy(
                     comments = null,
                     reply = null,
                  )
               }
            }
            .collect {
               loadComments(it)
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
         stateFlow.map { it.maxInput }
            .distinctUntilChanged()
            .collectLatest {
               inputState.fMaxLength(it)
            }
      }
   }

   data class State(
      val newsId: String = "",
      val isLoading: Boolean = false,

      val commentCount: Int? = null,
      val comments: List<NewsCommentGroupModel>? = null,

      val reply: NewsCommentModel? = null,

      val userId: String = "",
      val isSending: Boolean = false,
      val isDeleting: Boolean = false,

      val showInput: Boolean = false,
      val maxInput: Int = 250,
   )
}