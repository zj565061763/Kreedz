package com.sd.android.kreedz.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.data.repository.AppRepository

@Router(path = AppRouter.MAIN)
class MainActivity : BaseActivity() {
  @Composable
  override fun ContentImpl() {
    LaunchedEffect(Unit) {
      AppRepository().sync()
    }
    MainScreen(
      onExit = { finish() }
    )
  }
}