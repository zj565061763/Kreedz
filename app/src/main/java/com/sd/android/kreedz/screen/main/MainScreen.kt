package com.sd.android.kreedz.screen.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.data.event.ReClickMainNavigation
import com.sd.android.kreedz.data.model.MainNavigation
import com.sd.android.kreedz.feature.chat.screen.chatbox.ChatBoxScreen
import com.sd.android.kreedz.feature.records.screen.map.MapRecordsScreen
import com.sd.android.kreedz.screen.home.MainHomeScreen
import com.sd.android.kreedz.screen.more.MainMoreScreen
import com.sd.lib.compose.active.FSetActive
import com.sd.lib.compose.tab.container.TabContainer
import com.sd.lib.event.FEvent

@Composable
fun MainScreen(
   modifier: Modifier = Modifier,
   vm: MainVM = viewModel(),
   onExit: () -> Unit,
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val context = LocalContext.current

   Surface(modifier = modifier) {
      Column(modifier = Modifier.fillMaxSize()) {
         ContentView(
            modifier = Modifier.weight(1f),
            listNavigation = state.listNavigation,
            selectedNavigationIndex = state.selectedNavigationIndex,
         )
         MainNavigationView(
            listNavigation = state.listNavigation,
            selectedNavigationIndex = state.selectedNavigationIndex,
            onClickNavigation = { index ->
               if (state.selectedNavigationIndex == index) {
                  state.listNavigation.getOrNull(index)?.also { navigation ->
                     FEvent.post(ReClickMainNavigation(navigation))
                  }
               }
               vm.selectNavigation(index)
            },
         )
      }
   }

   var backPressedTime by remember { mutableLongStateOf(0L) }
   BackHandler {
      val current = System.currentTimeMillis()
      if (current - backPressedTime < 2_000) {
         onExit()
      } else {
         Toast.makeText(context, "One more press to exit.", Toast.LENGTH_SHORT).show()
      }
      backPressedTime = current
   }
}

@Composable
private fun ContentView(
   modifier: Modifier = Modifier,
   listNavigation: List<MainNavigation>,
   selectedNavigationIndex: Int,
) {
   TabContainer(
      modifier = modifier.fillMaxSize(),
      selectedKey = listNavigation[selectedNavigationIndex],
   ) {
      listNavigation.forEachIndexed { index, item ->
         tab(item) {
            FSetActive(index == selectedNavigationIndex) {
               when (item) {
                  MainNavigation.Home -> MainHomeScreen()
                  MainNavigation.Records -> MapRecordsScreen()
                  MainNavigation.ChatBox -> ChatBoxScreen()
                  MainNavigation.More -> MainMoreScreen()
               }
            }
         }
      }
   }
}