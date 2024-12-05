package com.sd.android.kreedz

import android.app.Application
import com.sd.android.kreedz.core.ModuleCore
import com.sd.lib.ctx.FContext

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    FContext.set(this)
    hookScrollableTabRowMinimumTabWidth()
    ModuleCore.init(
      context = this,
      isRelease = BuildConfig.BUILD_TYPE == "release",
    )
  }
}

private fun hookScrollableTabRowMinimumTabWidth() {
  runCatching {
    Class
      .forName("androidx.compose.material3.TabRowKt")
      .getDeclaredField("ScrollableTabRowMinimumTabWidth").apply {
        isAccessible = true
      }.set(null, 0f)
  }
}