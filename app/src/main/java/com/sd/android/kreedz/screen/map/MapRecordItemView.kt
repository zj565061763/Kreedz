package com.sd.android.kreedz.screen.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.RecordModel
import com.sd.android.kreedz.data.utils.DataUtils
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewSmall
import com.sd.android.kreedz.feature.common.ui.ComYoutubeButton
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
fun MapRecordItemView(
   modifier: Modifier = Modifier,
   record: RecordModel,
   currentRecord: RecordModel?,
   onClickPlayer: (userId: String) -> Unit,
) {
   var timeDifference by remember { mutableStateOf("") }

   if (record.id == currentRecord?.id) {
      timeDifference = "WR"
   } else {
      val time = record.time
      val currentTime = currentRecord?.time ?: time
      LaunchedEffect(time, currentTime) {
         val duration = (time - currentTime).coerceAtLeast(0)
         timeDifference = if (duration > 0) {
            "+${DataUtils.formatRecordTime(duration)}"
         } else ""
      }
   }

   ItemView(
      modifier = modifier,
      time = record.timeStr,
      timeDifference = timeDifference,
      country = record.player.country,
      playerName = record.player.nickname,
      youtubeLink = record.youtubeLink,
      date = record.dateStr,
      deleted = record.deleted,
      onClickPlayer = {
         onClickPlayer(record.player.id)
      },
   )
}

@Composable
private fun ItemView(
   modifier: Modifier = Modifier,
   time: String,
   timeDifference: String,
   country: String?,
   playerName: String,
   youtubeLink: String?,
   date: String,
   deleted: Boolean,
   onClickPlayer: () -> Unit,
) {
   ConstraintLayout(
      modifier = modifier
         .fillMaxWidth()
         .padding(8.dp)
   ) {
      val (
         refTime, refPlayer, refDeleted,
         refTimeDifference, refYoutube, refDate,
      ) = createRefs()

      // time
      Text(
         text = time,
         fontSize = 14.sp,
         color = MaterialTheme.colorScheme.primary,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.constrainAs(refTime) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
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

      // time difference
      Text(
         text = timeDifference,
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier.constrainAs(refTimeDifference) {
            start.linkTo(parent.start)
            top.linkTo(refTime.bottom, 6.dp)
         }
      )

      // youtube
      ComYoutubeButton(
         link = youtubeLink,
         modifier = Modifier.constrainAs(refYoutube) {
            end.linkTo(parent.end, (-4).dp)
            top.linkTo(parent.top, (-4).dp)
            goneIf(youtubeLink.isNullOrBlank())
         }
      )

      // date
      Text(
         text = date,
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier.constrainAs(refDate) {
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
         }
      )

      // deleted
      if (deleted) {
         Text(
            text = "Deleted",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.constrainAs(refDeleted) {
               end.linkTo(refYoutube.start)
               centerVerticallyTo(refPlayer)
            }
         )
      }
   }
}

@Preview
@Composable
private fun PreviewView() {
   AppTheme {
      Card(shape = MaterialTheme.shapes.small) {
         ItemView(
            time = "01:25.25",
            timeDifference = "00:02.21",
            country = "cn",
            playerName = "zhengjun",
            youtubeLink = "link",
            date = "2024-10-11",
            deleted = true,
            onClickPlayer = {},
         )
      }
   }
}