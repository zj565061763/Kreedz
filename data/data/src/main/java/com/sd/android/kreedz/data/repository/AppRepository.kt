package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.local.LocalAppConfig
import com.sd.lib.coroutines.fGlobalLaunch
import com.sd.lib.lifecycle.fAppOnStart
import com.sd.lib.lifecycle.fAppOnStop
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.li
import kotlinx.coroutines.flow.StateFlow

fun AppRepository(): AppRepository = AppRepositoryImpl

interface AppRepository {
  fun isLightModeFlow(): StateFlow<Boolean>
  suspend fun toggleLightMode()
  suspend fun sync()
}

private object AppRepositoryImpl : AppRepository, FLogger {
  private val _onlineRepository = OnlineRepository()

  override fun isLightModeFlow(): StateFlow<Boolean> {
    return LocalAppConfig.isLightModeFlow
  }

  override suspend fun toggleLightMode() {
    LocalAppConfig.toggleLightMode()
  }

  override suspend fun sync() {
    _onlineRepository.sync()
  }

  init {
    fGlobalLaunch {
      fAppOnStart {
        li { "App onStart" }
      }
    }
    fGlobalLaunch {
      fAppOnStop {
        li { "App onStop" }
      }
    }
  }
}