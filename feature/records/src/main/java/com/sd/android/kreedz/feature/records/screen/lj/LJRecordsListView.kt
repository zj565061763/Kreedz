package com.sd.android.kreedz.feature.records.screen.lj

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.GroupedLJRecordsModel
import com.sd.android.kreedz.data.model.LJRecordModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LJRecordsListView(
  modifier: Modifier = Modifier,
  records: List<GroupedLJRecordsModel>,
  lazyListState: LazyListState,
  onGroupIndexes: (Map<String, Int>) -> Unit,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    state = lazyListState,
    contentPadding = PaddingValues(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    records.forEach { group ->
      stickyHeader(
        key = group.type,
        contentType = "header",
      ) {
        HeaderView(text = group.type)
      }
      itemsIndexed(
        items = group.records,
        contentType = { _, _ -> "item" }
      ) { index, item ->
        Card(shape = MaterialTheme.shapes.extraSmall) {
          LJRecordsItemView(
            rank = index + 1,
            country = item.playerCountry,
            countryText = item.playerName,
            block = item.block,
            distance = item.distance,
            prestrafe = item.prestrafe,
            topspeed = item.topspeed,
            youtubeLink = item.youtubeLink,
          )
        }
      }
    }
  }

  val onGroupIndexesUpdated by rememberUpdatedState(onGroupIndexes)
  LaunchedEffect(records) {
    withContext(Dispatchers.IO) {
      val map = mutableMapOf<String, Int>()
      var index = 0
      for (item in records) {
        map[item.type] = index
        index += (item.records.size + 1)
      }
      onGroupIndexesUpdated(map.toMap())
    }
  }
}

@Composable
private fun HeaderView(
  modifier: Modifier = Modifier,
  text: String,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface)
      .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = text,
      fontSize = 18.sp,
      fontWeight = FontWeight.Medium,
    )
  }
}

@Preview
@Composable
private fun Preview() {
  val list = listOf(
    GroupedLJRecordsModel(
      type = "LongJump",
      records = listOf(
        LJRecordModel(
          playerCountry = "cn",
          playerName = "Lobelia",
          block = "258",
          distance = "259.09",
          prestrafe = "273.125",
          topspeed = "344.771",
          youtubeLink = "https://www.youtube.com/watch?v=UyaebwzVJxY",
        ),
        LJRecordModel(
          playerCountry = "se",
          playerName = "propane",
          block = "258",
          distance = "258.558",
          prestrafe = "273.8",
          topspeed = "346.8",
          youtubeLink = "https://www.youtube.com/watch?v=YQ82R7vU_xM",
        ),
      )
    )
  )

  AppTheme {
    LJRecordsListView(
      records = list,
      lazyListState = rememberLazyListState(),
      onGroupIndexes = {},
    )
  }
}