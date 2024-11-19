package com.sd.android.kreedz.screen.blog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.data.event.ReClickMainNavigation
import com.sd.android.kreedz.data.model.MainNavigation
import com.sd.android.kreedz.feature.common.ui.ComErrorView
import com.sd.lib.compose.active.fIsActive
import com.sd.lib.compose.paging.FUIStateRefresh
import com.sd.lib.compose.paging.fIsRefreshing
import com.sd.lib.event.FEvent
import kotlinx.coroutines.flow.filter

@Composable
fun LatestBlogScreen(
   modifier: Modifier = Modifier,
   vm: LatestBlogVM = viewModel(),
) {
   val blogs = vm.blogFlow.collectAsLazyPagingItems()
   val lazyListState = rememberLazyListState()
   val context = LocalContext.current

   AppPullToRefresh(
      modifier = modifier.fillMaxSize(),
      isRefreshing = blogs.fIsRefreshing(),
      onRefresh = { blogs.refresh() },
   ) {
      LatestBlogListView(
         lazyListState = lazyListState,
         blogs = blogs,
         onClickBlog = {
            AppRouter.blog(context, it.id)
         },
         onClickAuthor = {
            AppRouter.user(context, it)
         },
      )

      blogs.FUIStateRefresh(
         stateError = {
            ComErrorView(error = it)
         }
      )
   }

   if (fIsActive()) {
      LaunchedEffect(lazyListState) {
         FEvent.flowOf<ReClickMainNavigation>()
            .filter { it.navigation == MainNavigation.Home }
            .collect {
               lazyListState.scrollToItem(0)
            }
      }
   }
}