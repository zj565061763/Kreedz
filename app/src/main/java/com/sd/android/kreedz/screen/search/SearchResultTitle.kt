package com.sd.android.kreedz.screen.search

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun LazyListScope.searchResultTitle(
  title: String,
  count: Int,
) {
  item(
    key = "search result title:$title",
    contentType = "search result title"
  ) {
    TitleView(
      title = title,
      count = count,
      modifier = Modifier
        .fillParentMaxWidth()
        .padding(vertical = 8.dp),
    )
  }
}

@Composable
private fun TitleView(
  modifier: Modifier = Modifier,
  title: String,
  count: Int,
) {
  Text(
    text = "$title (${count})",
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    modifier = modifier,
  )
}

@Preview
@Composable
private fun Preview() {
  TitleView(
    title = "User",
    count = 10,
  )
}