package com.sd.android.kreedz.screen.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.sd.android.kreedz.core.ui.AppImage
import com.sd.android.kreedz.data.repository.MapImageRepository

@Composable
fun MapImageView(
  modifier: Modifier = Modifier,
  mapImage: String?,
  mapId: String,
) {
  val context = LocalContext.current
  val imageRequest = remember(context, mapImage) {
    ImageRequest.Builder(context)
      .data(mapImage)
      .networkCachePolicy(CachePolicy.DISABLED)
      .build()
  }

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    AppImage(
      modifier = Modifier.fillMaxWidth(),
      model = imageRequest,
      contentScale = ContentScale.FillWidth,
      contentDescription = "Map image",
    )

    if (isLoadingMapImage(mapId)) {
      CircularProgressIndicator()
    }
  }
}

@Composable
private fun isLoadingMapImage(mapId: String): Boolean {
  if (LocalInspectionMode.current) return false
  return remember(mapId) {
    MapImageRepository().getLoadingFlow(mapId)
  }.collectAsStateWithLifecycle(initialValue = false).value
}