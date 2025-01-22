package com.sd.android.kreedz.feature.news.screen.news

import android.os.Bundle
import androidx.compose.ui.platform.LocalUriHandler
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.export.fsUri
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.NEWS)
internal class NewsActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val id = intent.getStringExtra("id")
    if (id.isNullOrBlank()) {
      finish()
      return
    }

    setPageContent {
      val uriHandler = LocalUriHandler.current
      NewsScreen(
        id = id,
        onClickBack = { finish() },
        onClickOpenUri = { fsUri.openNewsUri(it, uriHandler) },
      )
    }
  }
}