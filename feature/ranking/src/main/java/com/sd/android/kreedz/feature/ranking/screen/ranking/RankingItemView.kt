package com.sd.android.kreedz.feature.ranking.screen.ranking

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewMedium
import com.sd.android.kreedz.feature.common.ui.ComTextLabelView

@Composable
internal fun RankingItemView(
  modifier: Modifier = Modifier,
  rank: Int,
  country: String?,
  countryText: String?,
  recordNumber: String,
  percentNumber: String,
) {
  ConstraintLayout(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp, horizontal = 16.dp),
  ) {
    val (
      refRank, refCountry,
      refNumber, refPercent,
    ) = createRefs()

    // rank
    Text(
      text = rank.toString(),
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.constrainAs(refRank) {
        start.linkTo(parent.start)
        top.linkTo(parent.top)
      }
    )

    // country
    ComCountryTextViewMedium(
      country = country,
      text = countryText,
      modifier = Modifier.constrainAs(refCountry) {
        start.linkTo(refRank.end, 12.dp)
        centerVerticallyTo(refRank)
      }
    )

    // records
    ComTextLabelView(
      text = recordNumber,
      label = "records",
      modifier = Modifier.constrainAs(refNumber) {
        start.linkTo(refCountry.start)
        top.linkTo(refCountry.bottom, 8.dp)
      }
    )

    // percent
    Text(
      text = "${percentNumber}%",
      fontSize = 12.sp,
      modifier = Modifier.constrainAs(refPercent) {
        end.linkTo(parent.end)
        centerVerticallyTo(parent)
      }
    )
  }
}

@Preview
@Composable
private fun PreviewItemView() {
  AppTheme {
    RankingItemView(
      rank = 1,
      country = "cn",
      countryText = "zhengjun",
      recordNumber = "100",
      percentNumber = "12.37",
    )
  }
}