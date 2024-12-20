package com.sd.android.kreedz.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.data.model.SearchNewsModel
import com.sd.lib.compose.annotated.fAnnotatedWithTarget

fun LazyListScope.searchResultNews(
  keyword: String,
  listNews: List<SearchNewsModel>?,
  onClickNews: (SearchNewsModel) -> Unit,
) {
  if (listNews == null) return

  searchResultTitle(
    title = "News",
    count = listNews.size,
  )

  items(listNews) { item ->
    Card(shape = MaterialTheme.shapes.extraSmall) {
      ItemView(
        keyword = keyword,
        title = item.title,
        dateStr = item.dateStr,
        extract = item.extract,
        modifier = Modifier
          .clickable { onClickNews(item) }
          .padding(8.dp),
      )
    }
  }
}

@Composable
private fun ItemView(
  modifier: Modifier = Modifier,
  keyword: String,
  title: String,
  dateStr: String,
  extract: String,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Text(
      text = title,
      fontSize = 14.sp,
      lineHeight = 18.sp,
      fontWeight = FontWeight.Medium,
    )
    Text(
      text = dateStr,
      color = AppTextColor.small,
      fontSize = 12.sp,
    )
    Text(
      text = extract.fAnnotatedWithTarget(keyword, ignoreCase = true),
      fontSize = 12.sp,
      lineHeight = 18.sp,
      color = AppTextColor.medium,
    )
  }
}

@Preview
@Composable
private fun PreviewItemView() {
  ItemView(
    keyword = "colcolx",
    title = "WR RELEASE #818 - 28 new world records",
    dateStr = "25/08/2024 17:51",
    extract = "...<p><strong>[user]88542|hk|colcolx[/user]</strong></p>...",
  )
}