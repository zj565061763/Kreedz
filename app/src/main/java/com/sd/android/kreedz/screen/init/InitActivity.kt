package com.sd.android.kreedz.screen.init

import android.os.Bundle
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

class InitActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setPageContent {
      InitScreen(
        onFinish = {
          AppRouter.main(this@InitActivity)
          finish()
        }
      )
    }
  }
}