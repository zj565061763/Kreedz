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
import com.sd.lib.compose.active.FSetActive
import com.sd.lib.compose.layer.LayerContainer
import com.sd.lib.compose.systemui.FStatusBarDark
import com.sd.lib.compose.systemui.FStatusBarLight
import com.sd.lib.compose.systemui.FSystemUI

abstract class BaseActivity : ComponentActivity() {
  private val _appRepository = AppRepository()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent { Content() }
  }

  @Composable
  private fun Content() {
    val isLight by _appRepository.isLightModeFlow().collectAsStateWithLifecycle()
    AppTheme(isLight = isLight) {
      FSystemUI {
        if (isLight) FStatusBarLight() else FStatusBarDark()
        FSetActive(true) {
          LayerContainer {
            ContentImpl()
          }
        }
      }
    }
  }

  @Composable
  protected abstract fun ContentImpl()
}