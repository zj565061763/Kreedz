package com.sd.android.kreedz.feature.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import com.sd.android.kreedz.core.export.fsUri
import com.sd.lib.compose.annotated.fAnnotatedTargets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CharSequence.comAnnotatedLink(): AnnotatedString {
   val content = this

   var links by remember { mutableStateOf(emptyList<String>()) }

   LaunchedEffect(content) {
      withContext(Dispatchers.IO) {
         links = URL_PATTERN.toRegex().findAll(content)
            .map { it.value }
            .toList()
      }
   }

   val linkStyle = SpanStyle(
      color = MaterialTheme.colorScheme.primary,
      textDecoration = TextDecoration.Underline,
   )

   val uriHandler = LocalUriHandler.current
   return content.fAnnotatedTargets(
      targets = links,
      onTarget = { target ->
         withLink(
            LinkAnnotation.Url(
               url = target,
               styles = TextLinkStyles(linkStyle),
               linkInteractionListener = {
                  fsUri.openUri(target, uriHandler)
               },
            )
         ) {
            append(target)
         }
      },
   )
}

private const val URL_PATTERN = "(https?://[\\w-]+(\\.[\\w-]+)+(/[^\\s]*)?)"