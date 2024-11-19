package com.sd.android.kreedz.screen.home

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel

class MainHomeVM : BaseViewModel<MainHomeVM.State, Unit>(State()) {

   fun selectTab(index: Int) {
      updateState {
         if (index in it.tabs.indices) {
            it.copy(selectedTabIndex = index)
         } else {
            it
         }
      }
   }

   @Immutable
   data class State(
      val tabs: List<MainHomeTab> = MainHomeTab.entries,
      val selectedTabIndex: Int = 0,
   )
}