package com.sd.android.kreedz.screen.user

import androidx.compose.runtime.Composable
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.lib.compose.utils.FFinish
import com.sd.lib.compose.utils.fIntentExtra

@Router(path = AppRouter.USER)
class UserActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      val id = fIntentExtra { it.getStringExtra("id") }
      if (id.isNullOrBlank()) {
         FFinish()
         return
      }

      UserScreen(
         userId = id,
         onClickBack = { finish() },
      )
   }
}