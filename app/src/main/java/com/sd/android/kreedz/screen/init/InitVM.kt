package com.sd.android.kreedz.screen.init

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import kotlinx.coroutines.delay

class InitVM : BaseViewModel<InitVM.State, Unit>(State()) {

   init {
      vmLaunch {
         delay(5_00)
         updateState { it.copy(finish = true) }
      }
   }

   @Immutable
   data class State(
      val finish: Boolean = false,
   )
}