package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sd.android.kreedz.core.export.fsUri

@Composable
fun ComYoutubeButton(
   link: String?,
   modifier: Modifier = Modifier,
) {
   val uriHandler = LocalUriHandler.current
   IconButton(
      modifier = modifier.size(24.dp),
      onClick = {
         fsUri.openUri(link, uriHandler)
      },
   ) {
      YoutubeImageView()
   }
}

@Composable
private fun YoutubeImageView(
   modifier: Modifier = Modifier,
) {
   Box(
      modifier = modifier
         .size(16.dp)
         .background(color = Color(0xFFEF4444), shape = CircleShape),
      contentAlignment = Alignment.Center,
   ) {
      Icon(
         modifier = Modifier.size(12.dp),
         imageVector = Icons.Default.PlayArrow,
         contentDescription = "Play youtube video",
         tint = Color.White,
      )
   }
}

@Preview
@Composable
private fun PreviewView() {
   ComYoutubeButton("url")
}