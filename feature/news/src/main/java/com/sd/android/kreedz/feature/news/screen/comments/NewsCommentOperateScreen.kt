package com.sd.android.kreedz.feature.news.screen.comments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sd.android.kreedz.core.utils.AppUtils
import com.sd.android.kreedz.data.model.NewsCommentModel
import com.sd.android.kreedz.feature.common.ui.ComInputLayer
import com.sd.android.kreedz.feature.common.ui.ComLoadingLayer

@Composable
internal fun NewsCommentOperateScreen(
  vm: NewsCommentVM,
  clickedComment: NewsCommentModel?,
  onDetachRequestCommentMenuLayer: () -> Unit,
) {
  val state by vm.stateFlow.collectAsStateWithLifecycle()
  val context = LocalContext.current

  ComInputLayer(
    attach = state.showInput,
    onDetachRequest = {
      vm.closeInput()
    },
    topLabel = state.reply?.let { reply ->
      "Replay ${reply.author.nickname}'s comment"
    } ?: "",
    inputState = vm.inputState,
    maxInput = state.maxInput,
    onClickSend = {
      vm.clickSend(context)
    },
  )

  NewsCommentMenuLayer(
    attach = clickedComment != null,
    onDetachRequest = onDetachRequestCommentMenuLayer,
    onClickReply = {
      clickedComment?.also { model ->
        vm.clickReply(context, model)
        onDetachRequestCommentMenuLayer()
      }
    },
    onClickCopy = {
      clickedComment?.also { model ->
        AppUtils.copyText(model.comment)
        onDetachRequestCommentMenuLayer()
      }
    },
    onClickDelete = if (
      state.userId.isNotBlank()
      && state.userId == clickedComment?.author?.id
    ) {
      {
        clickedComment.also { model ->
          vm.clickDelete(context, model)
          onDetachRequestCommentMenuLayer()
        }
      }
    } else null,
  )

  ComLoadingLayer(
    attach = state.isSending,
    onDetachRequest = { vm.cancelSend() },
  )

  ComLoadingLayer(
    attach = state.isDeleting,
    onDetachRequest = { vm.cancelDelete() },
  )
}