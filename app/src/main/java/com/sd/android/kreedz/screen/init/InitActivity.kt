package com.sd.android.kreedz.screen.init

import androidx.compose.runtime.Composable
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

class InitActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      InitScreen(
         onFinish = {
            AppRouter.main(this@InitActivity)
            finish()
         }
      )
   }
}