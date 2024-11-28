package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.sd.android.kreedz.core.export.fsUri
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.lib.compose.html.ComposeHtml
import com.sd.lib.compose.html.Factory
import com.sd.lib.compose.html.rememberComposeHtml
import com.sd.lib.compose.html.tags.Tag_img
import com.sd.lib.compose.utils.fEnabled
import com.sd.lib.xlog.fDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

@Composable
fun ComHtmlView(
   modifier: Modifier = Modifier,
   html: String,
   lineHeight: TextUnit = 20.sp,
) {
   val colorScheme = MaterialTheme.colorScheme
   val context = LocalContext.current
   val uriHandler = LocalUriHandler.current
   val density = LocalDensity.current

   val composeHtml = rememberComposeHtml().apply {
      Factory("a") {
         AppTag_a(
            style = SpanStyle(
               color = colorScheme.primary,
               textDecoration = TextDecoration.Underline,
            ),
            onClickUrl = {
               fsUri.openUri(it, uriHandler)
            },
            onClickBlog = {
               AppRouter.blog(context, it)
            },
            onClickNews = {
               AppRouter.news(context, it)
            },
         )
      }

      Factory("user") {
         AppTag_user(
            style = SpanStyle(
               fontWeight = FontWeight.SemiBold,
            ),
            onClickUser = {
               AppRouter.user(context, it)
            },
         )
      }

      Factory("map") {
         AppTag_map(
            style = SpanStyle(
               color = colorScheme.primary,
               fontWeight = FontWeight.Medium
            ),
            onClickMap = {
               AppRouter.map(context, it)
            },
         )
      }

      Factory("timer") {
         AppTag_timer(
            style = SpanStyle(
               color = colorScheme.primary,
               fontWeight = FontWeight.Medium,
            )
         )
      }

      Factory("ytLink") {
         AppTag_ytLink()
      }

      Factory("iframe") {
         AppTag_iframe(
            style = SpanStyle(
               color = colorScheme.primary,
               textDecoration = TextDecoration.Underline,
            ),
            onClickUrl = {
               fsUri.openUri(it, uriHandler)
            },
         )
      }

      Factory("url") {
         AppTag_url(
            style = SpanStyle(
               color = colorScheme.primary,
               textDecoration = TextDecoration.Underline,
            ),
            onClickUrl = {
               fsUri.openUri(it, uriHandler)
            },
         )
      }
   }

   BoxWithConstraints(modifier = modifier) {
      composeHtml.Factory("img") {
         AppTag_img(
            density = density,
            maxWidth = maxWidth,
            lineHeight = lineHeight,
         )
      }

      var annotated by remember { mutableStateOf(AnnotatedString("")) }
      LaunchedEffect(
         composeHtml,
         html,
         colorScheme,
         context,
         uriHandler,
         density,
         maxWidth,
         lineHeight,
      ) {
         withContext(Dispatchers.IO) {
            annotated = composeHtml.parse(hookHtml(html))
         }
      }

      val inlineContent by composeHtml.inlineContentFlow.collectAsStateWithLifecycle()

      Text(
         text = annotated,
         inlineContent = inlineContent,
         color = AppTextColor.medium,
         fontSize = 14.sp,
         lineHeight = lineHeight,
      )
   }
}

private class AppTag_a(
   private val style: SpanStyle,
   private val onClickUrl: (url: String) -> Unit,
   private val onClickBlog: (blogId: String) -> Unit,
   private val onClickNews: (newsId: String) -> Unit,
) : ComposeHtml.Tag() {
   override fun elementEnd(builder: AnnotatedString.Builder, element: Element, start: Int, end: Int) {
      val url = element.attr("href")
      val linkAnnotation = LinkAnnotation.Url(
         url = url,
         styles = TextLinkStyles(style),
         linkInteractionListener = {
            handleUrl(url)
         },
      )
      builder.addLink(
         url = linkAnnotation,
         start = start,
         end = end,
      )
   }

   private fun handleUrl(url: String) {
      when {
         url.startsWith("blog/") -> {
            url.removePrefix("blog/").toIntOrNull()?.also { id ->
               onClickBlog(id.toString())
            }
         }
         url.startsWith("news/") -> {
            url.removePrefix("news/").toIntOrNull()?.also { id ->
               onClickNews(id.toString())
            }
         }
         else -> onClickUrl(url)
      }
   }
}

private class AppTag_user(
   private val style: SpanStyle,
   private val onClickUser: (userId: String) -> Unit,
) : ComposeHtml.Tag() {
   override fun elementText(builder: AnnotatedString.Builder, element: Element, textNode: TextNode) {
      // [user]88542|hk|colcolx[/user]
      val array = textNode.text().split("|")

      val id = array.getOrNull(0)
      if (id.isNullOrBlank()) return

      val country = array.getOrNull(1)
      if (country.isNullOrBlank()) return

      val nickname = array.getOrNull(2)
      if (nickname.isNullOrBlank()) return

      val linkAnnotation = LinkAnnotation.Clickable(
         tag = id,
         styles = TextLinkStyles(style),
         linkInteractionListener = {
            onClickUser(id)
         }
      )

      builder.withLink(linkAnnotation) {
         appendInlineContent(id = country)
         append(nickname)
         addInlineContent(
            id = country,
            placeholderWidth = 18.sp,
            placeholderHeight = 1.em,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom,
         ) {
            Box(
               contentAlignment = Alignment.Center,
               modifier = Modifier
                  .fillMaxSize()
                  .padding(end = 2.dp),
            ) {
               ComCountryImageView(
                  country = country,
                  modifier = Modifier.fillMaxWidth(),
               )
            }
         }
      }
   }
}

private class AppTag_map(
   private val style: SpanStyle,
   private val onClickMap: (mapId: String) -> Unit,
) : ComposeHtml.Tag() {
   override fun elementText(builder: AnnotatedString.Builder, element: Element, textNode: TextNode) {
      // [map]794|8b1_brickngrass[/map]
      val array = textNode.text().split("|")

      val id = array.getOrNull(0)
      if (id.isNullOrBlank()) return

      val map = array.getOrNull(1)
      if (map.isNullOrBlank()) return

      val linkAnnotation = LinkAnnotation.Clickable(
         tag = id,
         styles = TextLinkStyles(style),
         linkInteractionListener = {
            onClickMap(id)
         }
      )

      builder.withLink(linkAnnotation) {
         append(map)
      }
   }
}

private class AppTag_timer(
   private val style: SpanStyle,
) : ComposeHtml.Tag() {
   override fun elementText(builder: AnnotatedString.Builder, element: Element, textNode: TextNode) {
      // [timer]12243|03:39.44[/timer]
      val array = textNode.text().split("|")

      val time = array.getOrNull(1)
      if (time.isNullOrBlank()) return

      builder.withStyle(style) {
         append(time)
         appendLine()
      }
   }
}

private class AppTag_ytLink : ComposeHtml.Tag() {
   override fun elementText(builder: AnnotatedString.Builder, element: Element, textNode: TextNode) {
      // [ytLink]https://youtu.be/Cs6-G1Ka6mA?si=-dcGazmHM0u6fD6Q[/ytLink]
      val text = textNode.text()
      if (!text.startsWith("http")) return

      builder.appendInlineContent(id = text)
      addInlineContent(
         id = text,
         placeholderWidth = 24.sp,
         placeholderHeight = 24.sp,
         placeholderVerticalAlign = PlaceholderVerticalAlign.Bottom,
         content = {
            ComYoutubeButton(
               link = text,
               modifier = Modifier.size(24.dp),
            )
         },
      )
   }
}

private class AppTag_iframe(
   private val style: SpanStyle,
   private val onClickUrl: (url: String) -> Unit,
) : ComposeHtml.Tag() {
   override fun elementEnd(builder: AnnotatedString.Builder, element: Element, start: Int, end: Int) {
      val url = element.attr("src")
      val linkAnnotation = LinkAnnotation.Url(
         url = url,
         styles = TextLinkStyles(style),
         linkInteractionListener = {
            onClickUrl(url)
         },
      )
      builder.withLink(linkAnnotation) {
         append(url)
      }
   }
}

private class AppTag_url(
   private val style: SpanStyle,
   private val onClickUrl: (url: String) -> Unit,
) : ComposeHtml.Tag() {
   override fun elementStart(builder: AnnotatedString.Builder, element: Element): Boolean {
      val url = element.attr("url")
      val linkAnnotation = LinkAnnotation.Url(
         url = url,
         styles = TextLinkStyles(style),
         linkInteractionListener = {
            onClickUrl(url)
         },
      )
      builder.withLink(linkAnnotation) {
         append(url)
      }
      // Skip children
      return true
   }
}

private class AppTag_img(
   density: Density,
   maxWidth: Dp,
   lineHeight: TextUnit,
) : Tag_img(
   density = density,
   maxWidth = maxWidth,
   lineHeight = lineHeight,
) {
   override fun addImgContent(src: String, alt: String, placeholder: Placeholder) {
      addInlineContent(
         id = src,
         placeholder = placeholder
      ) {
         val painter = rememberAsyncImagePainter(src)
         val loadState = painter.state

         Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
               .fillMaxSize()
               .fEnabled(loadState !is AsyncImagePainter.State.Success) {
                  background(MaterialTheme.colorScheme.surfaceContainer)
               },
         ) {
            Image(
               painter = painter,
               contentDescription = alt,
               contentScale = ContentScale.FillWidth,
               modifier = Modifier.fillMaxWidth(),
            )

            if (loadState is AsyncImagePainter.State.Error) {
               LaunchedEffect(Unit) {
                  fDebug { "html image $src error:${loadState.result.throwable.stackTraceToString()}" }
               }
            }
         }
      }
   }
}

private fun hookHtml(html: String): String {
   return html.replace(
      regex = CustomDataRegex.toRegex(),
      replacement = "<$1>$2</$1>",
   ).replace(
      regex = CustomUrlRegex.toRegex(),
      replacement = "<url url=$1>$2</url>",
   )
}

private const val CustomDataRegex = """\[(\w+)](.+?)\[/\1]"""
private const val CustomUrlRegex = """\[url=(https?://\S+?)](.+?)\[/url]"""