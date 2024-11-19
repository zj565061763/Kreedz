package com.sd.android.kreedz.feature.account.screen.login

import androidx.compose.runtime.Composable
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.LOGIN)
internal class LoginActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      LoginScreen(
         onClickBack = {
            finish()
         },
      )
   }
}