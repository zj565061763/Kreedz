package com.sd.android.kreedz.exportImpl

import androidx.compose.ui.platform.UriHandler
import com.didi.drouter.annotation.Service
import com.sd.android.kreedz.BuildConfig
import com.sd.android.kreedz.core.export.FSUri

@Service(function = [FSUri::class])
class FSUriImpl : FSUri {
  override fun openUri(uri: String?, uriHandler: UriHandler) {
    if (uri.isNullOrBlank()) return
    runCatching {
      uriHandler.openUri(uri)
    }.onFailure {
      it.printStackTrace()
    }
  }

  override fun openNewsUri(id: String, uriHandler: UriHandler) {
    if (id.isBlank()) return
    val uri = "https://${BuildConfig.HOST}/news/$id"
    openUri(uri, uriHandler)
  }

  override fun openBlogUri(id: String, uriHandler: UriHandler) {
    if (id.isBlank()) return
    val uri = "https://${BuildConfig.HOST}/blog/$id"
    openUri(uri, uriHandler)
  }
}