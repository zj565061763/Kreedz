package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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

@Preview
@Composable
private fun Preview() {
  Row(
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    ComTournamentIconView(rank = 1)
    ComTournamentIconView(rank = 2)
    ComTournamentIconView(rank = 3)
  }
}