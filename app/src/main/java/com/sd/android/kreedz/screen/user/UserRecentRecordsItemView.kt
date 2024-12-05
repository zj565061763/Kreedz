package com.sd.android.kreedz.screen.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
fun UserRecentRecordsItemView(
  modifier: Modifier = Modifier,
  map: String,
  time: String,
  currentRecord: Boolean,
  date: String?,
  news: String?,
  onClickNews: () -> Unit,
) {
  ConstraintLayout(
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp)
  ) {
    val (refMap, refTime, refNews, refDate) = createRefs()

    // Map
    Text(
      text = map,
      fontSize = 14.sp,
      modifier = Modifier.constrainAs(refMap) {
        start.linkTo(parent.start)
        top.linkTo(parent.top)
      }
    )

    // Time
    Text(
      text = time,
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.primary,
      fontWeight = FontWeight.Medium,
      textDecoration = if (currentRecord) null else TextDecoration.LineThrough,
      modifier = Modifier.constrainAs(refTime) {
        start.linkTo(refMap.start)
        top.linkTo(refMap.bottom, 6.dp)
      }
    )

    // date
    Text(
      text = date ?: "",
      fontSize = 12.sp,
      color = AppTextColor.medium,
      modifier = Modifier.constrainAs(refDate) {
        top.linkTo(parent.top)
        end.linkTo(parent.end)
      }
    )

    // News
    Text(
      text = news ?: "",
      fontSize = 12.sp,
      color = AppTextColor.medium,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier
        .constrainAs(refNews) {
          top.linkTo(refTime.bottom, 6.dp)
          start.linkTo(parent.start)
          goneIf(news.isNullOrBlank())
        }
        .clickable { onClickNews() }
    )
  }
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    UserRecentRecordsItemView(
      map = "slD_bside_winter",
      time = "01:05.95",
      currentRecord = false,
      date = "28/10/2024",
      news = "WR Relase #820 - 23 NEW WORLD RECORDS",
      onClickNews = {},
    )
  }
}

@Preview
@Composable
private fun PreviewEmptyDate() {
  AppTheme {
    UserRecentRecordsItemView(
      map = "slD_bside_winter",
      time = "01:05.95",
      currentRecord = false,
      date = null,
      news = "WR Relase #820 - 23 NEW WORLD RECORDS",
      onClickNews = {},
    )
  }
}

@Preview
@Composable
private fun PreviewEmptyNews() {
  AppTheme {
    UserRecentRecordsItemView(
      map = "slD_bside_winter",
      time = "01:05.95",
      currentRecord = false,
      date = "28/10/2024",
      news = null,
      onClickNews = {},
    )
  }
}

@Preview
@Composable
private fun PreviewEmptyDateAndNews() {
  AppTheme {
    UserRecentRecordsItemView(
      map = "slD_bside_winter",
      time = "01:05.95",
      currentRecord = false,
      date = null,
      news = null,
      onClickNews = {},
    )
  }
}