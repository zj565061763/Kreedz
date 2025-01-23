package com.sd.android.kreedz.core.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.repository.AppRepository
import com.sd.lib.compose.systemui.FStatusBarDark
import com.sd.lib.compose.systemui.FStatusBarLight
import com.sd.lib.compose.systemui.FSystemUI
import com.sd.lib.kmp.compose_active.FSetActive
import com.sd.lib.kmp.compose_layer.LayerContainer

abstract class BaseActivity : ComponentActivity() {
  private val _appRepository = AppRepository()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
  }

  protected fun setPageContent(
    content: @Composable () -> Unit,
  ) {
    setContent {
      val isLight by _appRepository.isLightModeFlow().collectAsStateWithLifecycle()
      AppTheme(isLight = isLight) {
        FSystemUI {
          if (isLight) FStatusBarLight() else FStatusBarDark()
          FSetActive(true) {
            LayerContainer {
              content()
            }
          }
        }
      }
    }
  }
}