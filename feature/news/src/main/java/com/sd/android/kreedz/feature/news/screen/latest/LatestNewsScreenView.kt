package com.sd.android.kreedz.feature.news.screen.latest

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.data.event.ReClickMainNavigation
import com.sd.android.kreedz.data.model.MainNavigation
import com.sd.android.kreedz.data.model.NewsModel
import com.sd.android.kreedz.feature.common.ui.ComErrorView
import com.sd.lib.compose.active.fIsActive
import com.sd.lib.compose.paging.FUIStateRefresh
import com.sd.lib.compose.paging.fIsRefreshing
import com.sd.lib.event.FEvent
import kotlinx.coroutines.flow.filter

@Composable
fun LatestNewsScreenView(
   modifier: Modifier = Modifier,
   items: LazyPagingItems<NewsModel>,
   onClickItem: (newsId: String) -> Unit,
) {
   val lazyListState = rememberLazyListState()

   AppPullToRefresh(
      modifier = modifier.fillMaxSize(),
      isRefreshing = items.fIsRefreshing(),
      onRefresh = { items.refresh() },
   ) {
      LatestNewsListView(
         lazyListState = lazyListState,
         news = items,
         onClickItem = onClickItem,
      )
      items.FUIStateRefresh(
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