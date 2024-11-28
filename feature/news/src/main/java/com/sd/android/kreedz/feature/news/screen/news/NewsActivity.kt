package com.sd.android.kreedz.feature.news.screen.news

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.export.fsUri
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.lib.compose.utils.FFinish
import com.sd.lib.compose.utils.fIntentExtra

@Router(path = AppRouter.NEWS)
internal class NewsActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      val id = fIntentExtra { it.getStringExtra("id") }
      if (id.isNullOrBlank()) {
         FFinish()
         return
      }

      val uriHandler = LocalUriHandler.current

      NewsScreen(
         id = id,
         onClickBack = {
            finish()
         },
         onClickOpenUri = {
            fsUri.openNewsUri(it, uriHandler)
         }
      )
   }
}