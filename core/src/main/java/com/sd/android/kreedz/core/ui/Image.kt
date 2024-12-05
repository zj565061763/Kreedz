package com.sd.android.kreedz.core.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter.State
import coil.compose.rememberAsyncImagePainter

@Composable
fun AppImage(
  model: Any?,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  alignment: Alignment = Alignment.Center,
  contentScale: ContentScale = ContentScale.Fit,
  alpha: Float = DefaultAlpha,
  colorFilter: ColorFilter? = null,

  placeholder: Painter? = null,
  error: Painter? = null,
  fallback: Painter? = error,
  onLoading: ((State.Loading) -> Unit)? = null,
  onSuccess: ((State.Success) -> Unit)? = null,
  onError: ((State.Error) -> Unit)? = null,
) {
  val painter = if (model is Int && LocalInspectionMode.current) {
    painterResource(model)
  } else {
    rememberAsyncImagePainter(
      model = model,
      placeholder = placeholder,
      error = error,
      fallback = fallback,
      onLoading = onLoading,
      onSuccess = onSuccess,
      onError = onError,
      contentScale = contentScale,
    )
  }
  Image(
    painter = painter,
    contentDescription = contentDescription,
    modifier = modifier,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter,
  )
}