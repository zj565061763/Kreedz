package com.sd.android.kreedz.feature.news.screen.news

import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
internal fun NewsContentView(
   modifier: Modifier = Modifier,
   htmlContent: String,
) {
   val spanned = remember {
      Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
   }

   Box(modifier = modifier) {
      AndroidView(
         factory = { context ->
            TextView(context)
         },
         update = { textView ->
            textView.text = spanned
         },
      )
   }
}