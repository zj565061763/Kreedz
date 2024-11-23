package com.sd.android.kreedz.feature.news.screen.release

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.RecordModel
import com.sd.android.kreedz.data.repository.MapRepository
import com.sd.android.kreedz.data.utils.DataUtils
import com.sd.android.kreedz.feature.common.ui.ComCountryImageView
import com.sd.android.kreedz.feature.common.ui.ComYoutubeButton
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
internal fun LatestRecordsItemView(
   modifier: Modifier = Modifier,
   current: RecordModel,
   previous: RecordModel?,
) {
   val currentTime = current.time
   val previousTime = previous?.time
   val timeDifference = remember(currentTime, previousTime) {
      if (previousTime == null) {
         ""
      } else {
         val difference = (previousTime - currentTime).coerceAtLeast(0)
         if (difference > 0) {
            "+${DataUtils.formatRecordTime(difference)}"
         } else ""
      }
   }
   ItemView(
      modifier = modifier,
      mapId = current.map.id,
      mapName = current.map.name,
      currentTime = current.timeStr,
      youtubeLink = current.youtubeLink,
      previousTime = previous?.timeStr,
      previousPlayerName = previous?.player?.nickname,
      previousPlayerCountry = previous?.player?.country,
      timeDifference = timeDifference,
   )
}

@Composable
private fun ItemView(
   modifier: Modifier = Modifier,
   mapId: String,
   mapName: String,
   currentTime: String,
   youtubeLink: String?,
   previousTime: String?,
   previousPlayerName: String?,
   previousPlayerCountry: String?,
   timeDifference: String?,
) {
   val isLoadingMap = isLoadingMap(mapId)

   ConstraintLayout(
      modifier = modifier
         .fillMaxWidth()
         .padding(8.dp)
   ) {
      val (
         refMapName, refCurrentTime, refYoutubeLink,
         refPrevious, refLoading,
      ) = createRefs()

      // mapName
      Text(
         text = mapName,
         fontSize = 14.sp,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.constrainAs(refMapName) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
         }
      )

      // currentTime
      Text(
         text = currentTime,
         fontSize = 14.sp,
         color = MaterialTheme.colorScheme.primary,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.constrainAs(refCurrentTime) {
            centerVerticallyTo(refMapName)
            start.linkTo(refMapName.end, 6.dp)
         }
      )

      PreviousView(
         time = previousTime,
         playerName = previousPlayerName,
         playerCountry = previousPlayerCountry,
         timeDifference = timeDifference,
         modifier = Modifier.constrainAs(refPrevious) {
            top.linkTo(refMapName.bottom, 4.dp)
            start.linkTo(refMapName.start)
         }
      )

      ComYoutubeButton(
         link = youtubeLink,
         modifier = Modifier.constrainAs(refYoutubeLink) {
            centerVerticallyTo(refMapName)
            end.linkTo(parent.end)
            goneIf(youtubeLink.isNullOrBlank())
         }
      )

      if (isLoadingMap) {
         CircularProgressIndicator(
            strokeWidth = 1.dp,
            modifier = Modifier.constrainAs(refLoading) {
               start.linkTo(refPrevious.end, 4.dp)
               centerVerticallyTo(refPrevious)
               width = 12.dp.asDimension()
               height = 12.dp.asDimension()
            }
         )
      }
   }
}

@Composable
private fun PreviousView(
   modifier: Modifier = Modifier,
   time: String?,
   playerName: String?,
   playerCountry: String?,
   timeDifference: String?,
) {
   Row(
      modifier = modifier.animateContentSize(),
      verticalAlignment = Alignment.CenterVertically,
   ) {
      if (playerCountry != null) {
         ComCountryImageView(
            country = playerCountry,
            modifier = Modifier.width(14.dp),
         )
      }
      Text(
         text = playerName ?: "N/A",
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier
            .padding(start = 2.dp)
            .alignByBaseline(),
      )
      Text(
         text = time ?: "",
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier
            .padding(start = 6.dp)
            .alignByBaseline(),
      )
      Text(
         text = timeDifference ?: "",
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier
            .padding(start = 6.dp)
            .alignByBaseline(),
      )
   }
}

@Composable
private fun isLoadingMap(mapId: String): Boolean {
   if (LocalInspectionMode.current) return false
   return remember(mapId) {
      MapRepository().getMapLoadingFlow(mapId)
   }.collectAsStateWithLifecycle(initialValue = false).value
}

@Preview
@Composable
private fun Preview() {
   AppTheme {
      Card(shape = MaterialTheme.shapes.extraSmall) {
         ItemView(
            mapId = "",
            mapName = "bkz_factory",
            currentTime = "01:59.12",
            youtubeLink = "youtubeLink",
            previousTime = "02:00.66",
            previousPlayerName = "topoviygus",
            previousPlayerCountry = "ru",
            timeDifference = "+00:01.25"
         )
      }
   }
}

@Preview
@Composable
private fun PreviewEmptyPrevious() {
   AppTheme {
      Card(shape = MaterialTheme.shapes.extraSmall) {
         ItemView(
            mapId = "",
            mapName = "bkz_factory",
            currentTime = "01:59.12",
            youtubeLink = "youtubeLink",
            previousTime = null,
            previousPlayerName = null,
            previousPlayerCountry = null,
            timeDifference = null,
         )
      }
   }
}