package com.sd.android.kreedz.feature.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextDecoration
import com.sd.android.kreedz.core.export.fsUri
import com.sd.lib.kmp.compose_annotated.annotatedWithRegex

@Composable
fun CharSequence.comAnnotatedLink(): AnnotatedString {
  val uriHandler = LocalUriHandler.current
  val linkStyle = SpanStyle(
    color = MaterialTheme.colorScheme.primary,
    textDecoration = TextDecoration.Underline,
  )
  return annotatedWithRegex(
    regex = UrlRegex,
    onTarget = { result ->
      val link = LinkAnnotation.Url(
        url = result.value,
        styles = TextLinkStyles(linkStyle),
        linkInteractionListener = {
          fsUri.openUri(result.value, uriHandler)
        },
      )
      addLink(
        url = link,
        start = result.range.first,
        end = result.range.last + 1,
      )
    },
  )
}

private val UrlRegex = "(https?://[\\w-]+(\\.[\\w-]+)+(/\\S*)?)".toRegex()