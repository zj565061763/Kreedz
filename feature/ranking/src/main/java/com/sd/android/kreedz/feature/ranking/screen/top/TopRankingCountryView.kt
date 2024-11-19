package com.sd.android.kreedz.feature.ranking.screen.top

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.TopCountryRankingModel

@Composable
internal fun TopRankingCountryView(
   modifier: Modifier = Modifier,
   topCountry: List<TopCountryRankingModel>,
   onClickMore: () -> Unit,
) {
   TopRankingCardView(
      modifier = modifier,
      title = "Country",
      onClickMore = onClickMore,
   ) {
      topCountry.forEach { item ->
         TopRankingCardItemView(
            country = item.country,
            text = item.countryName,
            number = item.numberOfRecords,
         )
      }
   }
}

@Preview
@Composable
private fun Preview() {
   val topCountry = listOf(
      TopCountryRankingModel(
         country = "ru",
         countryName = "Russia",
         numberOfRecords = "258",
      ),
      TopCountryRankingModel(
         country = "cz",
         countryName = "Czechia",
         numberOfRecords = "227",
      ),
      TopCountryRankingModel(
         country = "ar",
         countryName = "Argentina",
         numberOfRecords = "119",
      ),
   )
   AppTheme {
      TopRankingCountryView(
         topCountry = topCountry,
         onClickMore = {},
      )
   }
}