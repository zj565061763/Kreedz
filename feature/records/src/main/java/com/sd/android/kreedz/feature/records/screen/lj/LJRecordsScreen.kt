package com.sd.android.kreedz.feature.records.screen.lj

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.feature.common.ui.ComErrorView
import com.sd.android.kreedz.feature.common.ui.ComResultBox
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LJRecordsScreen(
   modifier: Modifier = Modifier,
   vm: LJRecordsVM = viewModel(),
   onClickBack: () -> Unit,
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
   val lazyListState = rememberLazyListState()
   val scope = rememberCoroutineScope()

   var groupIndexes by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

   Scaffold(
      modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
      topBar = {
         LJRecordsTitleView(
            scrollBehavior = scrollBehavior,
            onClickBack = onClickBack,
            groups = state.groups,
            onClickGroup = { group ->
               scope.launch {
                  groupIndexes[group]?.let {
                     lazyListState.scrollToItem(it)
                  }
               }
            },
         )
      },
   ) { padding ->
      ComResultBox(
         modifier = Modifier
            .fillMaxSize()
            .padding(padding),
         isLoading = state.isLoading,
         result = state.result,
         onFailure = {
            ComErrorView(
               error = it,
               onClickRetry = { vm.retry() }
            )
         },
      ) {
         LJRecordsListView(
            records = state.records,
            lazyListState = lazyListState,
            onGroupIndexes = { groupIndexes = it }
         )
      }
   }

   LaunchedEffect(vm) {
      vm.load()
   }
}