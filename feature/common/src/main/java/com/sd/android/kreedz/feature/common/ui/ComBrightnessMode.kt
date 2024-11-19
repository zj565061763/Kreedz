package com.sd.android.kreedz.feature.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sd.android.kreedz.data.repository.AppRepository
import kotlinx.coroutines.launch

@Composable
fun ComBrightnessModeView(
   content: @Composable BrightnessModeViewScope.() -> Unit,
) {
   val repository = remember { AppRepository() }
   val coroutineScope = rememberCoroutineScope()
   val isLight by repository.isLightModeFlow().collectAsStateWithLifecycle()

   val scopeImpl = remember(isLight) {
      object : BrightnessModeViewScope {
         override val isLightMode: Boolean
            get() = isLight

         override fun toggleMode() {
            coroutineScope.launch {
               repository.toggleLightMode()
            }
         }
      }
   }

   with(scopeImpl) {
      content()
   }
}

interface BrightnessModeViewScope {
   val isLightMode: Boolean
   fun toggleMode()
}