package com.sd.android.kreedz.feature.news.screen.news

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sd.android.kreedz.feature.common.ui.ComHtmlView

@Composable
internal fun NewsContentView(
  modifier: Modifier = Modifier,
  html: String,
) {
  ComHtmlView(
    modifier = modifier,
    html = html,
  )
}