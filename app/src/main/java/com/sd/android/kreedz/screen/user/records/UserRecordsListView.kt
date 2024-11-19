package com.sd.android.kreedz.screen.user.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.UserRecordModel
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
fun UserRecordsListView(
   modifier: Modifier = Modifier,
   records: List<UserRecordModel>,
   onClickItem: (UserRecordModel) -> Unit,
) {
   LazyColumn(
      modifier = modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(8.dp),
   ) {
      items(items = records) { item ->
         Card(
            shape = MaterialTheme.shapes.extraSmall,
            onClick = { onClickItem(item) }
         ) {
            ItemView(
               mapName = item.mapName,
               time = item.timeStr,
               date = item.dateStr,
               timeDifference = item.timeDifferenceStr,
            )
         }
      }
   }
}

@Composable
private fun ItemView(
   modifier: Modifier = Modifier,
   mapName: String,
   time: String,
   date: String,
   timeDifference: String?,
) {
   ConstraintLayout(
      modifier = modifier
         .fillMaxWidth()
         .padding(8.dp)
   ) {
      val (
         refMapName, refDate,
         refTime, refTimeDifference,
         refYoutube,
      ) = createRefs()

      // map name
      Text(
         text = mapName,
         fontSize = 14.sp,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.constrainAs(refMapName) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
         }
      )

      // record time
      Text(
         text = time,
         fontSize = 14.sp,
         color = MaterialTheme.colorScheme.primary,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.constrainAs(refTime) {
            start.linkTo(refMapName.start)
            top.linkTo(refMapName.bottom, 6.dp)
         }
      )

      // time difference
      Text(
         text = if (timeDifference.isNullOrBlank()) ""
         else "+$timeDifference",
         fontSize = 14.sp,
         color = AppTextColor.medium,
         modifier = Modifier.constrainAs(refTimeDifference) {
            start.linkTo(refTime.end, 8.dp)
            bottom.linkTo(refTime.bottom)
            goneIf(timeDifference.isNullOrBlank())
         }
      )

      // date
      Text(
         text = date,
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier.constrainAs(refDate) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
         }
      )
   }
}

@Preview
@Composable
private fun PreviewItemView() {
   AppTheme {
      Card(shape = MaterialTheme.shapes.small) {
         ItemView(
            mapName = "bkz_goldbhop",
            time = "01:25.25",
            timeDifference = "00:01.62",
            date = "2024-10-11",
         )
      }
   }
}