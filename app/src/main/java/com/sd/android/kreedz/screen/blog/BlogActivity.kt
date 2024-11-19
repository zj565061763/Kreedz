package com.sd.android.kreedz.screen.blog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.utils.AppUtils
import com.sd.lib.compose.utils.FFinish
import com.sd.lib.compose.utils.fIntentExtra

@Router(path = AppRouter.BLOG)
internal class BlogActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      val id = fIntentExtra { it.getStringExtra("id") }
      if (id.isNullOrBlank()) {
         FFinish()
         return
      }

      val url = "https://xtreme-jumps.eu/blog/$id"
      val uriHandler = LocalUriHandler.current
      LaunchedEffect(url, uriHandler) {
         AppUtils.handleLink(url, uriHandler)
         finish()
      }
   }
}