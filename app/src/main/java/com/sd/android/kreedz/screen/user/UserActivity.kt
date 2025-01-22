package com.sd.android.kreedz.screen.user

import android.os.Bundle
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.USER)
class UserActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val id = intent.getStringExtra("id")
    if (id.isNullOrBlank()) {
      finish()
      return
    }

    setPageContent {
      UserScreen(
        userId = id,
        onClickBack = { finish() },
      )
    }
  }
}