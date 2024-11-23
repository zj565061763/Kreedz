package com.sd.android.kreedz.screen.blog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.export.fsUri
import com.sd.android.kreedz.core.router.AppRouter
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

      val uriHandler = LocalUriHandler.current
      LaunchedEffect(id, uriHandler) {
         fsUri.openBlogUri(id, uriHandler)
         finish()
      }
   }
}