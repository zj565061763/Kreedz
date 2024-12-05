package com.sd.android.kreedz.screen.user

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.UserRecordModel
import com.sd.android.kreedz.feature.common.ui.ComTextLabelView
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
fun UserRecordsStatsView(
  modifier: Modifier = Modifier,
  isLoadingRecordsStats: Boolean,
  rank: String?,
  numberOfCurrentRecords: String?,
  numberOfTotalRecords: String?,
  numberOfMaps: String?,
  firstRecord: UserRecordModel?,
  lastRecord: UserRecordModel?,
  onClickRecord: (UserRecordModel) -> Unit,
) {
  ConstraintLayout(
    modifier = modifier
      .animateContentSize()
      .fillMaxWidth()
      .padding(8.dp)
  ) {
    val (
      refRank, refMoreRecords,
      refRecordsInfo, refFirstLastRecord,
      refLoading,
    ) = createRefs()

    RankView(
      rank = rank,
      modifier = Modifier.constrainAs(refRank) {
        start.linkTo(parent.start)
        top.linkTo(parent.top)
      }
    )

    Icon(
      imageVector = Icons.Default.MoreVert,
      contentDescription = "More records",
      modifier = Modifier.constrainAs(refMoreRecords) {
        centerVerticallyTo(refRank)
        end.linkTo(parent.end)
        width = Dimension.value(16.dp)
        height = Dimension.value(16.dp)
        goneIf(firstRecord == null)
      }
    )

    RecordsInfoView(
      numberOfCurrentRecords = numberOfCurrentRecords,
      numberOfTotalRecords = numberOfTotalRecords,
      numberOfMaps = numberOfMaps,
      modifier = Modifier.constrainAs(refRecordsInfo) {
        start.linkTo(parent.start)
        top.linkTo(refRank.bottom, 6.dp)
      }
    )

    FirstLastRecordView(
      firstRecord = firstRecord,
      lastRecord = lastRecord,
      onClickRecord = onClickRecord,
      modifier = Modifier.constrainAs(refFirstLastRecord) {
        start.linkTo(parent.start)
        top.linkTo(refRecordsInfo.bottom, 6.dp)
      }
    )

    if (isLoadingRecordsStats) {
      CircularProgressIndicator(
        strokeWidth = 1.dp,
        modifier = Modifier.constrainAs(refLoading) {
          centerVerticallyTo(parent)
          end.linkTo(parent.end)
          width = Dimension.value(16.dp)
          height = Dimension.value(16.dp)
        }
      )
    }
  }
}

@Composable
private fun RankView(
  modifier: Modifier = Modifier,
  rank: String?,
) {
  Row(modifier = modifier) {
    Text(
      text = "Records ranking: ",
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier.alignByBaseline(),
    )
    Text(
      text = rank.takeUnless { it.isNullOrBlank() } ?: "N/A",
      fontSize = 14.sp,
      fontWeight = FontWeight.SemiBold.takeUnless { rank.isNullOrBlank() },
      modifier = Modifier.alignByBaseline(),
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RecordsInfoView(
  modifier: Modifier = Modifier,
  numberOfCurrentRecords: String?,
  numberOfTotalRecords: String?,
  numberOfMaps: String?,
) {
  FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    ComTextLabelView(
      text = numberOfCurrentRecords ?: "0",
      label = "current",
    )

    ComTextLabelView(
      text = numberOfTotalRecords ?: "0",
      label = "total",
    )

    ComTextLabelView(
      text = numberOfMaps ?: "0",
      label = "maps",
    )
  }
}

@Composable
private fun FirstLastRecordView(
  modifier: Modifier = Modifier,
  firstRecord: UserRecordModel?,
  lastRecord: UserRecordModel?,
  onClickRecord: (UserRecordModel) -> Unit,
) {
  if (firstRecord == null && lastRecord == null) return
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    firstRecord?.also { record ->
      RecordView(
        label = "First",
        mapName = record.mapName,
        date = record.dateStr,
        modifier = Modifier.clickable {
          onClickRecord(record)
        },
      )
    }
    lastRecord?.also { record ->
      RecordView(
        label = "Last",
        mapName = record.mapName,
        date = record.dateStr,
        modifier = Modifier.clickable {
          onClickRecord(record)
        },
      )
    }
  }
}

@Composable
private fun RecordView(
  modifier: Modifier = Modifier,
  label: String,
  mapName: String?,
  date: String?,
) {
  if (mapName.isNullOrBlank()) return

  val content = remember(label, mapName, date) {
    buildAnnotatedString {
      append("$label: ")
      withStyle(
        SpanStyle(
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium,
        )
      ) {
        append(mapName)
      }
      if (!date.isNullOrBlank()) {
        append(" on $date")
      }
    }
  }

  Text(
    modifier = modifier,
    text = content,
    fontSize = 12.sp,
  )
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    UserRecordsStatsView(
      isLoadingRecordsStats = true,
      rank = "1",
      numberOfCurrentRecords = "171",
      numberOfTotalRecords = "264",
      numberOfMaps = "220",
      firstRecord = UserRecordModel(
        mapName = "fu_sane",
        timeStr = "05:09.35",
        dateStr = "08/05/2017",
        mapId = null,
        timeDifferenceStr = null,
      ),
      lastRecord = UserRecordModel(
        mapName = "spm_hb_paslalala",
        timeStr = "03:11.06",
        dateStr = "24/05/2024",
        mapId = null,
        timeDifferenceStr = null,
      ),
      onClickRecord = {},
    )
  }
}