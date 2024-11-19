package com.sd.android.kreedz.feature.records.screen.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.data.model.MapRecordModel
import com.sd.android.kreedz.data.model.UserModel
import com.sd.android.kreedz.feature.common.ui.ComMapRecordsItemView
import com.sd.android.kreedz.feature.common.ui.ComTextLabelView

@Composable
internal fun MapRecordsListView(
   modifier: Modifier = Modifier,
   records: List<MapRecordModel>,
   keywords: List<String>,
   lazyListState: LazyListState,
   onClickItem: (MapRecordModel) -> Unit,
   onClickPlayer: (UserModel) -> Unit,
) {
   LazyColumn(
      modifier = modifier.fillMaxSize(),
      state = lazyListState,
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(8.dp),
   ) {
      item(
         key = "items count",
         contentType = "items count",
      ) {
         Box(modifier = Modifier.fillParentMaxWidth()) {
            ComTextLabelView(
               text = records.size.toString(),
               label = "items",
               textColor = AppTextColor.small,
               textFontSize = 12.sp,
               modifier = Modifier.align(Alignment.Center),
            )
         }
      }

      items(
         items = records,
         key = { it.map.id },
      ) { item ->
         Card(
            shape = MaterialTheme.shapes.extraSmall,
            onClick = { onClickItem(item) }
         ) {
            ComMapRecordsItemView(
               item = item,
               keywords = keywords,
               onClickPlayer = {
                  item.record?.player?.also { player ->
                     onClickPlayer(player)
                  }
               }
            )
         }
      }
   }
}