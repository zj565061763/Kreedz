package com.sd.android.kreedz.feature.ranking.screen.top

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.TopPlayerRankingModel

@Composable
internal fun TopPlayerView(
   modifier: Modifier = Modifier,
   topPlayer: List<TopPlayerRankingModel>,
   onClickPlayer: (TopPlayerRankingModel) -> Unit,
   onClickMore: () -> Unit,
) {
   if (topPlayer.isEmpty()) return
   TopRankingCardView(
      modifier = modifier,
      title = "Player",
      onClickMore = onClickMore,
   ) {
      topPlayer.forEach { item ->
         TopRankingCardItemView(
            country = item.playerCountry,
            text = item.playerName,
            number = item.numberOfRecords,
            modifier = Modifier.clickable {
               onClickPlayer(item)
            }
         )
      }
   }
}

@Preview
@Composable
private fun Preview() {
   val topPlayer = listOf(
      TopPlayerRankingModel(
         playerCountry = "ru",
         playerName = "topoviygus",
         numberOfRecords = "171",
         playerId = "",
      ),
      TopPlayerRankingModel(
         playerCountry = "cz",
         playerName = "shooting-star",
         numberOfRecords = "124",
         playerId = "",
      ),
      TopPlayerRankingModel(
         playerCountry = "cz",
         playerName = "fykseN",
         numberOfRecords = "100",
         playerId = "",
      ),
   )
   AppTheme {
      TopPlayerView(
         topPlayer = topPlayer,
         onClickPlayer = {},
         onClickMore = {},
      )
   }
}