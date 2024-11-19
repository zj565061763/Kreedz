package com.sd.android.kreedz.feature.news.screen.release

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.core.utils.AppUtils
import com.sd.android.kreedz.data.event.ReClickMainNavigation
import com.sd.android.kreedz.data.model.LatestRecordGroupModel
import com.sd.android.kreedz.data.model.MainNavigation
import com.sd.android.kreedz.data.model.NewsCommentGroupModel
import com.sd.android.kreedz.data.model.NewsCommentModel
import com.sd.android.kreedz.data.model.RecordModel
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewLarge
import com.sd.android.kreedz.feature.common.ui.ComErrorEffect
import com.sd.android.kreedz.feature.common.ui.ComInputLayer
import com.sd.android.kreedz.feature.common.ui.ComLoadingDialog
import com.sd.android.kreedz.feature.news.screen.comments.NewsCommentMenuLayer
import com.sd.android.kreedz.feature.news.screen.comments.NewsCommentVM
import com.sd.android.kreedz.feature.news.screen.comments.newsCommentsView
import com.sd.lib.compose.active.fIsActive
import com.sd.lib.compose.utils.fClick
import com.sd.lib.event.FEvent
import kotlinx.coroutines.flow.filter

@Composable
fun LatestReleaseScreen(
   modifier: Modifier = Modifier,
   vm: LatestReleaseVM = viewModel(),
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val lazyListState = rememberLazyListState()
   val context = LocalContext.current

   val commentVM = viewModel<NewsCommentVM>()
   val commentState by commentVM.stateFlow.collectAsStateWithLifecycle()

   var clickComment by remember { mutableStateOf<NewsCommentModel?>(null) }

   AppPullToRefresh(
      modifier = modifier.fillMaxSize(),
      isRefreshing = state.isLoading,
      onRefresh = {
         vm.refresh()
         commentVM.refresh()
      },
   ) {
      ListView(
         lazyListState = lazyListState,
         title = state.newsName,
         groups = state.records,
         comments = commentState.comments,
         onClickTitle = {
            state.newsId?.also { newsId ->
               AppRouter.news(context, newsId)
            }
         },
         onClickPlayer = {
            AppRouter.user(context, it)
         },
         onClickItem = {
            AppRouter.map(context, it.map.id)
         },
         onClickAddComment = {
            commentVM.clickAdd(context)
         },
         onClickComment = {
            clickComment = it
         }
      )
   }

   LaunchedEffect(vm) {
      vm.init()
   }

   LaunchedEffect(commentVM, state.newsId) {
      commentVM.load(state.newsId ?: "")
   }

   vm.effectFlow.ComErrorEffect()
   commentVM.effectFlow.ComErrorEffect()

   if (fIsActive()) {
      LaunchedEffect(lazyListState) {
         FEvent.flowOf<ReClickMainNavigation>()
            .filter { it.navigation == MainNavigation.Home }
            .collect {
               lazyListState.scrollToItem(0)
            }
      }
   }

   ComInputLayer(
      attach = commentState.showInput,
      onDetachRequest = {
         commentVM.closeInput()
      },
      topLabel = commentState.reply?.let { reply ->
         "Replay ${reply.author.nickname}'s comment"
      } ?: "",
      inputState = commentVM.inputState,
      maxInput = commentState.maxInput,
      onClickSend = {
         commentVM.clickSend(context)
      },
   )

   NewsCommentMenuLayer(
      attach = clickComment != null,
      onDetachRequest = {
         clickComment = null
      },
      onClickReply = {
         clickComment?.also { model ->
            commentVM.clickReply(context, model)
            clickComment = null
         }
      },
      onClickCopy = {
         clickComment?.also { model ->
            AppUtils.copyText(model.message)
            clickComment = null
         }
      },
      onClickDelete = if (
         commentState.userId.isNotBlank()
         && commentState.userId == clickComment?.author?.id
      ) {
         {
            clickComment?.also { model ->
               commentVM.clickDelete(context, model)
               clickComment = null
            }
         }
      } else null,
   )

   if (commentState.isSending) {
      ComLoadingDialog {
         commentVM.cancelSend()
      }
   }

   if (commentState.isDeleting) {
      ComLoadingDialog {
         commentVM.cancelDelete()
      }
   }
}

@Composable
private fun ListView(
   modifier: Modifier = Modifier,
   lazyListState: LazyListState,
   title: String?,
   groups: List<LatestRecordGroupModel>,
   comments: List<NewsCommentGroupModel>,
   onClickTitle: () -> Unit,
   onClickPlayer: (userId: String) -> Unit,
   onClickItem: (RecordModel) -> Unit,
   onClickAddComment: () -> Unit,
   onClickComment: (NewsCommentModel) -> Unit,
) {
   LazyColumn(
      modifier = modifier.fillMaxSize(),
      state = lazyListState,
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(8.dp),
   ) {
      titleView(
         title = title,
         onClickTitle = onClickTitle,
      )

      recordsView(
         groups = groups,
         onClickPlayer = onClickPlayer,
         onClickItem = onClickItem,
      )

      if (groups.isNotEmpty()) {
         newsCommentsView(
            comments = comments,
            onClickAuthor = onClickPlayer,
            onClickAddComment = onClickAddComment,
            onClickComment = onClickComment,
         )
      }
   }
}

private fun LazyListScope.titleView(
   title: String?,
   onClickTitle: () -> Unit,
) {
   if (title.isNullOrBlank()) return
   item(
      key = "title",
      contentType = "title",
   ) {
      Card(
         modifier = Modifier.fillParentMaxWidth(),
         shape = MaterialTheme.shapes.extraSmall,
         colors = CardDefaults.cardColors(containerColor = Color.Transparent),
         onClick = onClickTitle,
      ) {
         Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(8.dp)
         )
      }
   }
}

private fun LazyListScope.recordsView(
   groups: List<LatestRecordGroupModel>,
   onClickPlayer: (userId: String) -> Unit,
   onClickItem: (RecordModel) -> Unit,
) {
   for (group in groups) {
      item(
         key = group.player.id,
         contentType = "player",
      ) {
         ComCountryTextViewLarge(
            country = group.player.country,
            text = group.player.nickname,
            modifier = Modifier
               .fClick { onClickPlayer(group.player.id) }
               .padding(8.dp),
         )
      }
      items(
         items = group.records,
         key = { it.current.id },
      ) { item ->
         Card(shape = MaterialTheme.shapes.extraSmall) {
            LatestRecordsItemView(
               current = item.current,
               previous = item.previous,
               modifier = Modifier.clickable {
                  onClickItem(item.current)
               }
            )
         }
      }
   }
}