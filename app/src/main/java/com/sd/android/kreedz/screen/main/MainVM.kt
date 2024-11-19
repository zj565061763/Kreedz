package com.sd.android.kreedz.screen.main

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.MainNavigation

class MainVM : BaseViewModel<MainVM.State, Unit>(State()) {

   fun selectNavigation(index: Int) {
      if (index in state.listNavigation.indices) {
         updateState { it.copy(selectedNavigationIndex = index) }
      }
   }

   @Immutable
   data class State(
      val listNavigation: List<MainNavigation> = MainNavigation.entries,
      val selectedNavigationIndex: Int = 0,
   )
}