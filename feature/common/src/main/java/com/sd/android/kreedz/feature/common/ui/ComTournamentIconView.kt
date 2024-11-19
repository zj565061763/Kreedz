package com.sd.android.kreedz.feature.common.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sd.android.kreedz.feature.common.R

@Composable
fun ComTournamentIconView(
   modifier: Modifier = Modifier,
   rank: Int,
   width: Dp = 14.dp,
) {
   val icon = when (rank) {
      1 -> R.drawable.cup1
      2 -> R.drawable.cup2
      else -> R.drawable.cup3
   }

   Image(
      modifier = modifier.width(width),
      painter = painterResource(icon),
      contentDescription = "Rank $rank",
      contentScale = ContentScale.FillWidth,
   )
}

@Composable
private fun IconView(
   @DrawableRes id: Int,
   contentDescription: String?,
   modifier: Modifier = Modifier,
   imageWidth: Dp = 14.dp,
) {
   Image(
      modifier = modifier.width(imageWidth),
      painter = painterResource(id),
      contentDescription = contentDescription,
      contentScale = ContentScale.FillWidth,
   )
}