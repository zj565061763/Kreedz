package com.sd.android.kreedz.core.export

import androidx.compose.ui.platform.UriHandler
import com.didi.drouter.api.DRouter

interface FSUri {
  fun openUri(uri: String?, uriHandler: UriHandler)
  fun openNewsUri(id: String, uriHandler: UriHandler)
  fun openBlogUri(id: String, uriHandler: UriHandler)
}

val fsUri: FSUri
  get() = DRouter.build(FSUri::class.java).getService()