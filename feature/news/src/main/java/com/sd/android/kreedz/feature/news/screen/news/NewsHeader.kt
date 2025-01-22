package com.sd.android.kreedz.feature.news.screen.news

import androidx.compose.foundation.lazy.LazyListScope
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.lib.kmp.compose_utils.fPin

internal fun LazyListScope.newsTitleView(
  title: String,
) {
  item(
    key = "news:title",
    contentType = "news:title",
  ) {
    fPin()
    NewsTitleView(title = title)
  }
}

internal fun LazyListScope.newsAuthorInfoView(
  authorCountry: String?,
  authorNickname: String?,
  authorIcons: UserIconsModel?,
  dateStr: String,
  onClickAuthor: () -> Unit,
) {
  item(
    key = "news:author info",
    contentType = "news:author info",
  ) {
    fPin()
    NewsAuthorInfoView(
      authorCountry = authorCountry,
      authorNickname = authorNickname,
      authorIcons = authorIcons,
      dateStr = dateStr,
      onClickAuthor = onClickAuthor,
    )
  }
}

internal fun LazyListScope.newsContentView(
  html: String,
) {
  item(
    key = "news:content",
    contentType = "news:content",
  ) {
    fPin()
    NewsContentView(html = html)
  }
}