package com.sd.android.kreedz.feature.news.screen.news

import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.data.model.NewsCommentGroupModel
import com.sd.android.kreedz.data.model.NewsCommentModel
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.feature.common.ui.ComEffect
import com.sd.android.kreedz.feature.news.screen.comments.NewsAddCommentButton
import com.sd.android.kreedz.feature.news.screen.comments.NewsCommentOperateScreen
import com.sd.android.kreedz.feature.news.screen.comments.NewsCommentVM
import com.sd.android.kreedz.feature.news.screen.comments.newsCommentsView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewsScreen(
   modifier: Modifier = Modifier,
   id: String,
   onClickBack: () -> Unit,
) {
   val vm = viewModel<NewsVM>()
   val state by vm.stateFlow.collectAsStateWithLifecycle()

   val commentVM = viewModel<NewsCommentVM>()
   val commentState by commentVM.stateFlow.collectAsStateWithLifecycle()
   var clickComment by remember { mutableStateOf<NewsCommentModel?>(null) }

   val context = LocalContext.current
   val uriHandler = LocalUriHandler.current
   val lazyListState = rememberLazyListState()

   val showTitle by remember {
      derivedStateOf {
         lazyListState.firstVisibleItemIndex > 0
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
                     vm.openUri(uriHandler)
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
      floatingActionButton = {
         NewsAddCommentButton(
            visible = state.title.isNotBlank()
               && !lazyListState.isScrollInProgress,
            onClick = {
               commentVM.clickAdd(context)
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
            lazyListState = lazyListState,
            title = state.title,
            htmlContent = state.htmlContent,
            author = state.author,
            dateStr = state.dateStr,
            isLoadingComments = commentState.isLoading,
            commentCount = commentState.commentCount,
            comments = commentState.comments,
            onClickComment = {
               clickComment = it
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
   lazyListState: LazyListState,
   title: String?,
   htmlContent: String,
   author: UserWithIconsModel?,
   dateStr: String,
   isLoadingComments: Boolean,
   commentCount: Int?,
   comments: List<NewsCommentGroupModel>?,
   onClickComment: (NewsCommentModel) -> Unit,
   onClickUser: (userId: String) -> Unit,
) {
   LazyColumn(
      modifier = modifier.fillMaxSize(),
      state = lazyListState,
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(
         start = 8.dp, end = 8.dp,
         top = 8.dp, bottom = 64.dp,
      ),
   ) {
      titleView(title = title)

      if (author != null) {
         infoView(
            authorCountry = author.country,
            authorNickname = author.nickname,
            authorIcons = author.icons,
            dateStr = dateStr,
            onClickAuthor = {
               onClickUser(author.id)
            }
         )
      }

      contentView(htmlContent = htmlContent)

      if (!title.isNullOrBlank()) {
         newsCommentsView(
            isLoadingComments = isLoadingComments,
            commentCount = commentCount,
            comments = comments,
            onClickAuthor = onClickUser,
            onClickComment = onClickComment,
         )
      }
   }
}

private fun LazyListScope.titleView(
   title: String?,
) {
   if (title.isNullOrBlank()) return
   item(
      key = "news title",
      contentType = "news title",
   ) {
      Text(
         text = title,
         fontSize = 24.sp,
         fontWeight = FontWeight.Medium,
      )
   }
}

private fun LazyListScope.infoView(
   authorCountry: String?,
   authorNickname: String?,
   authorIcons: UserIconsModel,
   dateStr: String,
   onClickAuthor: () -> Unit,
) {
   item(
      key = "news author",
      contentType = "news author",
   ) {
      NewsInfoView(
         authorCountry = authorCountry,
         authorNickname = authorNickname,
         authorIcons = authorIcons,
         dateStr = dateStr,
         onClickAuthor = onClickAuthor,
      )
   }
}

private fun LazyListScope.contentView(
   htmlContent: String,
) {
   if (htmlContent.isBlank()) return
   item(
      key = "news content",
      contentType = "news content",
   ) {
      NewsContentView(htmlContent = htmlContent)
   }
}