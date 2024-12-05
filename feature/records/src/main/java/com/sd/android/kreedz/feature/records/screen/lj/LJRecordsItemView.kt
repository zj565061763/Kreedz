package com.sd.android.kreedz.feature.records.screen.lj

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewMedium
import com.sd.android.kreedz.feature.common.ui.ComTextLabelView
import com.sd.android.kreedz.feature.common.ui.ComYoutubeButton
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
internal fun LJRecordsItemView(
  modifier: Modifier = Modifier,
  rank: Int,
  country: String?,
  countryText: String,
  block: String,
  distance: String,
  prestrafe: String,
  topspeed: String,
  youtubeLink: String?,
) {
  var expand by remember { mutableStateOf(false) }

  ConstraintLayout(
    modifier = modifier
      .fillMaxWidth()
      .clickable { expand = !expand }
      .padding(vertical = 12.dp),
  ) {
    val (refRank, refPlayer, refInfo, refYoutube) = createRefs()

    // rank
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.constrainAs(refRank) {
        start.linkTo(parent.start)
        top.linkTo(parent.top)
      }
    ) {
      Text(
        text = rank.toString(),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.widthIn(40.dp)
      )
    }

    // player
    ComCountryTextViewMedium(
      country = country,
      text = countryText,
      modifier = Modifier.constrainAs(refPlayer) {
        start.linkTo(refRank.end)
        top.linkTo(refRank.top)
      }
    )

    MainInfoView(
      block = block,
      distance = distance,
      prestrafe = prestrafe,
      topspeed = topspeed,
      expand = expand,
      modifier = Modifier.constrainAs(refInfo) {
        start.linkTo(refRank.end)
        top.linkTo(refPlayer.bottom)
      }
    )

    // youtube
    ComYoutubeButton(
      link = youtubeLink,
      modifier = Modifier.constrainAs(refYoutube) {
        centerVerticallyTo(parent)
        end.linkTo(parent.end, 12.dp)
        goneIf(youtubeLink.isNullOrBlank())
      }
    )
  }
}

@Composable
private fun MainInfoView(
  modifier: Modifier = Modifier,
  block: String,
  distance: String,
  prestrafe: String,
  topspeed: String,
  expand: Boolean,
) {
  Column(modifier = modifier.animateContentSize()) {
    Row(
      modifier = Modifier.padding(top = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      // block
      ComTextLabelView(
        text = block,
        label = "block",
      )
      // distance
      ComTextLabelView(
        text = distance,
        label = "distance",
      )
    }

    if (expand) {
      Row(
        modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        // prestrafe
        ComTextLabelView(
          text = prestrafe,
          label = "pre",
          textColor = AppTextColor.small,
        )

        // topspeed
        ComTextLabelView(
          text = topspeed,
          label = "speed",
          textColor = AppTextColor.small,
        )
      }
    }
  }
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    LJRecordsItemView(
      rank = 1,
      country = "cn",
      countryText = "Lobelia",
      block = "258",
      distance = "259.09",
      prestrafe = "273.125",
      topspeed = "344.771",
      youtubeLink = "youtubeLink",
    )
  }
}