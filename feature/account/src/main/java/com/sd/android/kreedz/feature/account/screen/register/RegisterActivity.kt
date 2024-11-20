package com.sd.android.kreedz.feature.account.screen.register

import androidx.compose.runtime.Composable
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.REGISTER)
internal class RegisterActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      RegisterScreen(
         onClickBack = {
            finish()
         },
      )
   }
}