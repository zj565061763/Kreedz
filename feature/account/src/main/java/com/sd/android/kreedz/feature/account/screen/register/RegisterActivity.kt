package com.sd.android.kreedz.feature.account.screen.register

import android.os.Bundle
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.REGISTER)
internal class RegisterActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setPageContent {
      RegisterScreen(
        onClickBack = { finish() },
      )
    }
  }
}