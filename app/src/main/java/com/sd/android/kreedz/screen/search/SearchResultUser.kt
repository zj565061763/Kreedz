package com.sd.android.kreedz.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sd.android.kreedz.data.model.SearchUserModel
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewSmall
import com.sd.lib.compose.annotated.fAnnotatedTargets

fun LazyListScope.searchResultUser(
  keyword: String,
  listUser: List<SearchUserModel>?,
  onClickUser: (SearchUserModel) -> Unit,
) {
  if (listUser == null) return

  searchResultTitle(
    title = "User",
    count = listUser.size,
  )

  items(listUser) { item ->
    Card(shape = MaterialTheme.shapes.extraSmall) {
      ItemView(
        keyword = keyword,
        nickname = item.nickname ?: "",
        country = item.country,
        modifier = Modifier
          .clickable { onClickUser(item) }
          .padding(8.dp),
      )
    }
  }
}

@Composable
private fun ItemView(
  modifier: Modifier = Modifier,
  keyword: String,
  nickname: String,
  country: String?,
) {
  ComCountryTextViewSmall(
    modifier = modifier,
    country = country,
    text = nickname.fAnnotatedTargets(
      targets = listOf(keyword),
      ignoreCase = true,
    )
  )
}

@Preview
@Composable
private fun PreviewItemView() {
  ItemView(
    keyword = "topo",
    nickname = "topoviygus",
    country = "ru",
  )
}