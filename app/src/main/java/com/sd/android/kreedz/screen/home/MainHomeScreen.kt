package com.sd.android.kreedz.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.feature.blog.screen.latest.LatestBlogScreen
import com.sd.android.kreedz.feature.news.screen.latest.LatestNewsScreen
import com.sd.android.kreedz.feature.news.screen.release.LatestReleaseScreen
import com.sd.android.kreedz.feature.ranking.screen.top.TopRankingScreen
import com.sd.android.kreedz.screen.servers.GameServerScreen
import com.sd.android.kreedz.screen.team.TeamScreen
import com.sd.lib.kmp.compose_active.FActiveContent
import com.sd.lib.kmp.compose_active.FSetActivePager
import com.sd.lib.kmp.compose_pager.FOnCurrentPage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(
  modifier: Modifier = Modifier,
  vm: MainHomeVM = viewModel(),
) {
  val state by vm.stateFlow.collectAsStateWithLifecycle()
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  val pagerState = rememberPagerState { state.tabs.size }
  pagerState.FOnCurrentPage { vm.selectTab(it) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        expandedHeight = 48.dp,
        title = {
          MainHomeTabView(
            tabs = state.tabs,
            selectedTabIndex = state.selectedTabIndex,
            onClickTab = { index ->
              scope.launch {
                pagerState.animateScrollToPage(index)
              }
            },
            onClickSearch = {
              AppRouter.search(context = context)
            },
          )
        },
      )
    },
  ) { padding ->
    HorizontalPager(
      state = pagerState,
      beyondViewportPageCount = state.tabs.size,
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
    ) { index ->
      FSetActivePager(pagerState, index) {
        state.tabs.getOrNull(index)?.also { tab ->
          TabContent(tab)
        }
      }
    }
  }
}

@Composable
private fun TabContent(
  tab: MainHomeTab,
) {
  FActiveContent {
    when (tab) {
      MainHomeTab.News -> LatestNewsScreen()
      MainHomeTab.Release -> LatestReleaseScreen()
      MainHomeTab.Ranking -> TopRankingScreen()
      MainHomeTab.Servers -> GameServerScreen()
      MainHomeTab.Blog -> LatestBlogScreen()
      MainHomeTab.Team -> TeamScreen()
    }
  }
}