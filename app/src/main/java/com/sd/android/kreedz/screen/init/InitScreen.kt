package com.sd.android.kreedz.screen.init

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun InitScreen(
   vm: InitVM = viewModel(),
   onFinish: () -> Unit,
) {
   val onFinishUpdated by rememberUpdatedState(onFinish)
   val state by vm.stateFlow.collectAsStateWithLifecycle()

   LaunchedEffect(state, state.finish) {
      if (state.finish) {
         onFinishUpdated()
      }
   }
}