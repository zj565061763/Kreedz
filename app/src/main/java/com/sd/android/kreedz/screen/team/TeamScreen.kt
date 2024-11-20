package com.sd.android.kreedz.screen.team

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.feature.common.ui.ComEffect

@Composable
internal fun TeamScreen(
   modifier: Modifier = Modifier,
   vm: TeamVM = viewModel(),
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val context = LocalContext.current

   AppPullToRefresh(
      modifier = modifier.fillMaxSize(),
      isRefreshing = state.isLoading,
      onRefresh = { vm.refresh() },
   ) {
      TeamListView(
         roles = state.roles,
         onClickUser = {
            AppRouter.user(context, it)
         },
      )
   }

   LaunchedEffect(vm) {
      vm.init()
   }

   vm.effectFlow.ComEffect()
}