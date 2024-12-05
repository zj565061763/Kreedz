package com.sd.android.kreedz.screen.user

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.feature.common.ui.ComTournamentIconView
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
fun UserAchievementsItemView(
  modifier: Modifier = Modifier,
  title: String,
  rank: Int?,
  date: String?,
) {
  ConstraintLayout(
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp)
  ) {
    val (refRank, refTitle, refDate) = createRefs()

    // Rank
    ComTournamentIconView(
      rank = rank ?: 0,
      modifier = Modifier.constrainAs(refRank) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        goneIf(rank == null)
      }
    )

    // Title
    Text(
      text = title,
      fontSize = 14.sp,
      modifier = Modifier.constrainAs(refTitle) {
        top.linkTo(parent.top)
        start.linkTo(refRank.end, 6.dp)
      }
    )

    // Date
    Text(
      text = date ?: "",
      fontSize = 12.sp,
      color = AppTextColor.medium,
      modifier = Modifier.constrainAs(refDate) {
        top.linkTo(refTitle.bottom, 6.dp)
        start.linkTo(refTitle.start)
        goneIf(date.isNullOrBlank())
      }
    )
  }
}

@Preview
@Composable
private fun Preview() {
  UserAchievementsItemView(
    title = "XJ Spring Tournament 2v2 2021",
    rank = 1,
    date = "25/04/2021",
  )
}

@Preview
@Composable
private fun PreviewEmptyDate() {
  UserAchievementsItemView(
    title = "XJ Spring Tournament 2v2 2021",
    rank = 1,
    date = null,
  )
}

@Preview
@Composable
private fun PreviewEmptyRank() {
  UserAchievementsItemView(
    title = "XJ Spring Tournament 2v2 2021",
    rank = null,
    date = "25/04/2021",
  )
}

@Preview
@Composable
private fun PreviewEmptyDateAndRank() {
  UserAchievementsItemView(
    title = "XJ Spring Tournament 2v2 2021",
    rank = null,
    date = null,
  )
}