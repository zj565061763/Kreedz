package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.MapRecordModel
import com.sd.lib.compose.annotated.fAnnotatedTargets
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
fun ComMapRecordsItemView(
   modifier: Modifier = Modifier,
   item: MapRecordModel,
   keywords: List<String>,
   onClickPlayer: () -> Unit,
) {
   val map = item.map
   val record = item.record
   val player = record?.player
   ItemView(
      modifier = modifier,
      mapName = map.name.fAnnotatedTargets(keywords, ignoreCase = true),
      time = record?.timeStr,
      country = player?.country,
      playerName = player?.nickname?.fAnnotatedTargets(keywords, ignoreCase = true),
      date = record?.dateStr,
      youtubeLink = record?.youtubeLink,
      onClickPlayer = onClickPlayer,
   )
}

@Composable
private fun ItemView(
   modifier: Modifier = Modifier,
   mapName: AnnotatedString,
   time: String?,
   country: String?,
   playerName: AnnotatedString?,
   date: String?,
   youtubeLink: String?,
   onClickPlayer: () -> Unit,
) {
   ConstraintLayout(
      modifier = modifier
         .fillMaxWidth()
         .padding(8.dp)
   ) {
      val (
         refMapName,
         refTime, refPlayer,
         refYoutube, refDate,
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
         text = time.takeUnless { it.isNullOrBlank() } ?: "N/A",
         fontSize = 14.sp,
         color = MaterialTheme.colorScheme.primary,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.constrainAs(refTime) {
            start.linkTo(refMapName.start)
            top.linkTo(refMapName.bottom, 6.dp)
         }
      )

      // player
      ComCountryTextViewSmall(
         country = country,
         text = playerName,
         modifier = Modifier
            .constrainAs(refPlayer) {
               start.linkTo(refTime.end, 8.dp)
               centerVerticallyTo(refTime)
            }
            .clickable { onClickPlayer() }
      )

      // youtube
      ComYoutubeButton(
         link = youtubeLink,
         modifier = Modifier.constrainAs(refYoutube) {
            end.linkTo(parent.end, (-4).dp)
            bottom.linkTo(parent.bottom, (-4).dp)
            goneIf(youtubeLink.isNullOrBlank())
         }
      )

      // date
      Text(
         text = date ?: "",
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier.constrainAs(refDate) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            goneIf(date.isNullOrBlank())
         }
      )
   }
}

@Preview
@Composable
private fun Preview() {
   AppTheme {
      Card(shape = MaterialTheme.shapes.small) {
         ItemView(
            mapName = AnnotatedString("bkz_goldbhop"),
            time = "01:25.25",
            country = "cn",
            playerName = AnnotatedString("zhengjun"),
            youtubeLink = "link",
            date = "2024-10-11",
            onClickPlayer = {},
         )
      }
   }
}

@Preview
@Composable
private fun PreviewNoRecord() {
   AppTheme {
      Card(shape = MaterialTheme.shapes.small) {
         ItemView(
            mapName = AnnotatedString("bkz_goldbhop"),
            time = null,
            country = null,
            playerName = null,
            date = null,
            youtubeLink = null,
            onClickPlayer = {},
         )
      }
   }
}