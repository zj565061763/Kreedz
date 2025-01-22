package com.sd.android.kreedz.screen.main

import android.os.Bundle
import androidx.compose.runtime.LaunchedEffect
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.data.repository.AppRepository

@Router(path = AppRouter.MAIN)
class MainActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setPageContent {
      LaunchedEffect(Unit) {
        AppRepository().sync()
      }
      MainScreen(
        onExit = { finish() }
      )
    }
  }
}