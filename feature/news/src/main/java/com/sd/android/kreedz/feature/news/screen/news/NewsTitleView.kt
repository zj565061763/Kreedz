package com.sd.android.kreedz.feature.news.screen.news

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
internal fun NewsTitleView(
  modifier: Modifier = Modifier,
  title: String,
) {
  Text(
    modifier = modifier,
    text = title,
    fontSize = 24.sp,
    fontWeight = FontWeight.Medium,
  )
}

@Preview
@Composable
private fun Preview() {
  NewsTitleView(
    title = "WR Release #820 - 23 NEW WORLD RECORDS",
  )
}