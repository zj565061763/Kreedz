package com.sd.android.kreedz.feature.news.screen.news

import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.data.model.NewsCommentGroupModel
import com.sd.android.kreedz.data.model.NewsCommentModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.feature.common.ui.ComEffect
import com.sd.android.kreedz.feature.news.screen.comments.NewsCommentOperateScreen
import com.sd.android.kreedz.feature.news.screen.comments.NewsCommentVM
import com.sd.android.kreedz.feature.news.screen.comments.newsCommentsView
import com.sd.lib.compose.nested.NestedHeader
import com.sd.lib.compose.nested.NestedHeaderState
import com.sd.lib.compose.nested.rememberNestedHeaderState
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
   modifier: Modifier = Modifier,
   id: String,
   vm: NewsVM = viewModel(),
   commentVM: NewsCommentVM = viewModel(),
   onClickBack: () -> Unit,
   onClickOpenUri: (newsId: String) -> Unit,
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val commentState by commentVM.stateFlow.collectAsStateWithLifecycle()
   var clickComment by remember { mutableStateOf<NewsCommentModel?>(null) }

   val context = LocalContext.current
   val density = LocalDensity.current
   val nestedHeaderState = rememberNestedHeaderState()
   val lazyListState = rememberLazyListState()

   val showTitle by remember {
      derivedStateOf {
         nestedHeaderState.offset.absoluteValue > density.run { 64.dp.toPx() }
      }
   }

   Scaffold(
      modifier = modifier,
      topBar = {
         TopAppBar(
            title = {
               if (showTitle) {
                  Text(
                     text = state.title,
                     modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        spacing = MarqueeSpacing(48.dp),
                     ),
                  )
               }
            },
            navigationIcon = {
               IconButton(onClick = onClickBack) {
                  Icon(
                     imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                     contentDescription = "Back",
                  )
               }
            },
            actions = {
               if (state.id.isNotBlank()) {
                  IconButton(onClick = {
                     onClickOpenUri(state.id)
                  }) {
                     Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Open uri",
                     )
                  }
               }
            },
         )
      },
   ) { padding ->
      AppPullToRefresh(
         modifier = Modifier
            .fillMaxSize()
            .padding(padding),
         isRefreshing = state.isLoading,
         onRefresh = {
            vm.refresh()
            commentVM.refresh()
         }
      ) {
         BodyView(
            nestedHeaderState = nestedHeaderState,
            lazyListState = lazyListState,
            title = state.title,
            html = state.html,
            author = state.author,
            dateStr = state.dateStr,
            isLoadingComments = commentState.isLoading,
            commentCount = commentState.commentCount,
            comments = commentState.comments,
            onClickComment = {
               clickComment = it
            },
            onClickAddComment = {
               commentVM.clickAdd(context)
            },
            onClickUser = {
               AppRouter.user(context, it)
            },
         )
      }
   }

   LaunchedEffect(vm, id) {
      vm.load(id)
   }

   if (state.title.isNotBlank()) {
      LaunchedEffect(commentVM, id) {
         commentVM.load(id)
      }
   }

   vm.effectFlow.ComEffect()
   commentVM.effectFlow.ComEffect()

   LaunchedEffect(commentVM, nestedHeaderState, lazyListState) {
      commentVM.effectFlow.collect { effect ->
         when (effect) {
            NewsCommentVM.Effect.AddNewComment -> {
               nestedHeaderState.hideHeader()
               lazyListState.scrollToItem(lazyListState.layoutInfo.totalItemsCount)
            }
         }
      }
   }

   NewsCommentOperateScreen(
      vm = commentVM,
      clickedComment = clickComment,
      onDetachRequestCommentMenuLayer = {
         clickComment = null
      },
   )
}

@Composable
private fun BodyView(
   modifier: Modifier = Modifier,
   nestedHeaderState: NestedHeaderState,
   lazyListState: LazyListState,
   title: String,
   html: String,
   author: UserWithIconsModel?,
   dateStr: String,
   isLoadingComments: Boolean,
   commentCount: Int?,
   comments: List<NewsCommentGroupModel>?,
   onClickComment: (NewsCommentModel) -> Unit,
   onClickAddComment: () -> Unit,
   onClickUser: (userId: String) -> Unit,
) {
   NestedHeader(
      modifier = modifier.fillMaxSize(),
      state = nestedHeaderState,
      header = {
         NewsHeaderView(
            title = title,
            html = html,
            authorCountry = author?.country,
            authorNickname = author?.nickname,
            authorIcons = author?.icons,
            dateStr = dateStr,
            onClickAuthor = {
               author?.also { onClickUser(it.id) }
            }
         )
      }
   ) {
      LazyColumn(
         modifier = Modifier.fillMaxSize(),
         state = lazyListState,
         verticalArrangement = Arrangement.spacedBy(8.dp),
         contentPadding = PaddingValues(
            start = 8.dp, end = 8.dp,
            top = 8.dp, bottom = 128.dp,
         ),
      ) {
         if (title.isNotBlank()) {
            newsCommentsView(
               isLoadingComments = isLoadingComments,
               commentCount = commentCount,
               comments = comments,
               onClickAuthor = onClickUser,
               onClickComment = onClickComment,
               onClickAddComment = onClickAddComment,
            )
         }
      }
   }
}