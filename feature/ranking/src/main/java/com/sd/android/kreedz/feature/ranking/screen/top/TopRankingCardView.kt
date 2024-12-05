package com.sd.android.kreedz.feature.ranking.screen.top

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.feature.common.ui.ComCountryImageView

@Composable
internal fun TopRankingCardView(
  modifier: Modifier = Modifier,
  title: String,
  onClickMore: () -> Unit,
  content: @Composable ColumnScope.() -> Unit,
) {
  Card(modifier = modifier) {
    TitleView(
      text = title,
      onClick = onClickMore,
    )
    content()
    Spacer(modifier = Modifier.height(8.dp))
  }
}

@Composable
internal fun TopRankingCardItemView(
  modifier: Modifier = Modifier,
  country: String?,
  text: String,
  number: String,
) {
  SlotItemView(
    modifier = modifier,
    left = {
      ComCountryImageView(
        country = country,
        modifier = Modifier.width(20.dp),
      )
    },
    center = {
      Text(
        text = text,
        fontSize = 14.sp,
      )
    },
    right = {
      Text(
        text = number,
        fontSize = 14.sp,
      )
    },
  )
}

@Composable
private fun SlotItemView(
  modifier: Modifier = Modifier,
  left: @Composable () -> Unit,
  center: @Composable () -> Unit,
  right: @Composable () -> Unit,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .heightIn(36.dp)
  ) {
    Box(
      modifier = Modifier
        .align(Alignment.CenterStart)
        .padding(start = 16.dp)
    ) {
      left()
    }

    Box(
      modifier = Modifier.align(Alignment.Center)
    ) {
      center()
    }

    Box(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(end = 16.dp)
    ) {
      right()
    }
  }
}

@Composable
private fun TitleView(
  modifier: Modifier = Modifier,
  text: String,
  onClick: () -> Unit,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 8.dp)
      .padding(top = 12.dp, bottom = 8.dp),
  ) {
    Text(
      text = text,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier.align(Alignment.CenterStart),
    )

    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForward,
      contentDescription = "More",
      modifier = Modifier.align(Alignment.CenterEnd),
    )
  }
}

@Preview
@Composable
private fun PreviewCardView() {
  AppTheme {
    TopRankingCardView(
      title = "Player",
      onClickMore = {},
      content = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          TopRankingCardItemView(
            country = "ru",
            text = "topoviygus",
            number = "175",
          )
          TopRankingCardItemView(
            country = "cz",
            text = "shooting-star",
            number = "126",
          )
        }
      },
    )
  }
}

@Preview
@Composable
private fun PreviewTitleView() {
  AppTheme {
    TitleView(
      text = "title",
      onClick = {},
    )
  }
}

@Preview
@Composable
private fun PreviewItemView() {
  AppTheme {
    TopRankingCardItemView(
      country = "cn",
      text = "zhengjun",
      number = "100",
    )
  }
}