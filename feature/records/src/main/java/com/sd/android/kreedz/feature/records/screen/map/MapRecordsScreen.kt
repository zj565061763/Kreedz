package com.sd.android.kreedz.feature.records.screen.map

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.data.event.ReClickMainNavigation
import com.sd.android.kreedz.data.model.MainNavigation
import com.sd.android.kreedz.data.model.MapRecordModel
import com.sd.android.kreedz.data.model.UserModel
import com.sd.android.kreedz.feature.common.ui.ComEffect
import com.sd.lib.kmp.compose_refresh.FRefreshContainer
import com.sd.lib.kmp.compose_refresh.rememberRefreshStateTop
import com.sd.lib.kmp.coroutines.FEvent
import com.sd.lib.kmp.coroutines.flowOf
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapRecordsScreen(
  modifier: Modifier = Modifier,
  vm: MapRecordsVM = viewModel(),
) {
  val state by vm.stateFlow.collectAsStateWithLifecycle()
  val context = LocalContext.current

  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val refreshState = rememberRefreshStateTop(state.isRefreshing) { vm.refresh() }

  Scaffold(
    modifier = modifier
      .nestedScroll(refreshState.nestedScrollConnection)
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      Box {
        MapRecordsTitleView(
          scrollBehavior = scrollBehavior,
          textFieldState = vm.textFieldState,
          onClickIconSort = {
            vm.toggleSortsVisibility()
          },
          onClickLongjumps = {
            AppRouter.ljRecords(context)
          },
        )
        if (state.isLoading) {
          LinearProgressIndicator(
            modifier = Modifier
              .fillMaxWidth()
              .height(1.dp)
              .align(Alignment.BottomCenter),
          )
        }
      }
    },
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      BodyView(
        topAppBarState = scrollBehavior.state,
        records = state.records,
        showSorts = state.showSorts,
        currentSort = state.currentSort,
        keywords = state.keywords,
        onClickSort = {
          vm.clickSort(it)
        },
        onClickItem = {
          AppRouter.map(context, it.map.id)
        },
        onClickPlayer = {
          AppRouter.user(context, it.id)
        }
      )
      FRefreshContainer(
        state = refreshState,
        modifier = Modifier.align(Alignment.TopCenter),
      )
    }
  }

  val hasKeyword by remember {
    derivedStateOf {
      vm.textFieldState.text.toString().isNotBlank()
    }
  }

  BackHandler(hasKeyword) {
    vm.textFieldState.clearText()
  }

  vm.effectFlow.ComEffect()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BodyView(
  modifier: Modifier = Modifier,
  topAppBarState: TopAppBarState,
  records: List<MapRecordModel>,
  showSorts: Boolean,
  keywords: List<String>,
  currentSort: MapRecordsSortModel,
  onClickSort: (MapRecordsSortType) -> Unit,
  onClickItem: (MapRecordModel) -> Unit,
  onClickPlayer: (UserModel) -> Unit,
) {
  val lazyListState = rememberLazyListState()

  Column(modifier = modifier.fillMaxSize()) {
    AnimatedVisibility(showSorts) {
      MapRecordsSortView(
        currentSort = currentSort,
        onClickSort = onClickSort,
      )
    }
    MapRecordsListView(
      modifier = Modifier.weight(1f),
      records = records,
      keywords = keywords,
      lazyListState = lazyListState,
      onClickItem = onClickItem,
      onClickPlayer = onClickPlayer,
    )
  }

  LaunchedEffect(lazyListState, topAppBarState) {
    FEvent.flowOf<ReClickMainNavigation>()
      .filter { it.navigation == MainNavigation.Records }
      .collect {
        lazyListState.scrollToItem(0)
        topAppBarState.heightOffset = 0f
      }
  }

  LaunchedEffect(lazyListState, keywords) {
    lazyListState.scrollToItem(0)
  }

  LaunchedEffect(lazyListState, currentSort) {
    lazyListState.scrollToItem(0)
  }
}