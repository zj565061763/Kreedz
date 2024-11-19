package com.sd.android.kreedz.feature.ranking.screen.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.lib.date.FDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RankingScreenView(
   modifier: Modifier = Modifier,
   title: String,
   isLoading: Boolean,
   onClickBack: () -> Unit,
   onRefresh: (FDate?) -> Unit,
   onSelectDate: (FDate?) -> Unit,
   content: LazyListScope.() -> Unit,
) {
   val dateVM = viewModel<RankingDateVM>()
   val dateState by dateVM.stateFlow.collectAsStateWithLifecycle()
   val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
   val lazyListState = rememberLazyListState()

   Scaffold(
      modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
      topBar = {
         RankingTitleView(
            title = title,
            selectedDateStr = dateState.selectedDateStr,
            scrollBehavior = scrollBehavior,
            onClickBack = onClickBack,
            onClickDate = { dateVM.showSelectDate() },
         )
      },
   ) { padding ->
      AppPullToRefresh(
         modifier = Modifier.padding(padding),
         isRefreshing = isLoading,
         onRefresh = { onRefresh(dateState.selectedDate) },
      ) {
         LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
            content = content,
         )
      }
   }

   RankingDateScreen(vm = dateVM)

   val onSelectDateUpdated by rememberUpdatedState(onSelectDate)
   LaunchedEffect(dateState.selectedDate) {
      lazyListState.scrollToItem(0)
      onSelectDateUpdated(dateState.selectedDate)
   }
}