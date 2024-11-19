package com.sd.android.kreedz.feature.common.screen

import androidx.compose.runtime.Composable
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.lib.compose.utils.FFinish
import com.sd.lib.compose.utils.fIntentExtra

@Router(path = AppRouter.WEB)
internal class WebActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      val url = fIntentExtra { it.getStringExtra("url") }
      if (url.isNullOrBlank()) {
         FFinish()
         return
      }

      WebScreen(
         url = url,
         onClickBack = { finish() },
      )
   }
}