package com.sd.android.kreedz.feature.common.ui

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.feature.common.R
import com.sd.lib.kmp.compose_utils.fEnabled

@Composable
fun ComUserIconsView(
  modifier: Modifier = Modifier,
  icons: UserIconsModel,
  enableClick: Boolean = true,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(1.dp),
  ) {
    if (icons.isVip) {
      VIPIcon(enableClick = enableClick)
    }

    if (icons.isRecordHolder) {
      RecordHolderIcon(enableClick = enableClick)
    }

    if (icons.isLJRecordHolder) {
      LJRecordHolderIcon(enableClick = enableClick)
    }

    when {
      icons.isTournamentRank1 -> ComTournamentIconView(rank = 1)
      icons.isTournamentRank2 -> ComTournamentIconView(rank = 2)
      icons.isTournamentRank3 -> ComTournamentIconView(rank = 3)
      else -> {}
    }

    if (icons.isMapper) {
      MapperIcon(enableClick = enableClick)
    }

    if (icons.isMovieEditor) {
      MovieEditorIcon(enableClick = enableClick)
    }
  }
}

@Composable
private fun VIPIcon(
  modifier: Modifier = Modifier,
  enableClick: Boolean,
) {
  IconView(
    modifier = modifier,
    id = R.drawable.vip,
    contentDescription = "VIP",
    enableClick = enableClick,
  )
}

@Composable
private fun RecordHolderIcon(
  modifier: Modifier = Modifier,
  enableClick: Boolean,
) {
  IconView(
    modifier = modifier,
    id = R.drawable.record_holder,
    contentDescription = "Record Holder",
    enableClick = enableClick,
  )
}

@Composable
private fun LJRecordHolderIcon(
  modifier: Modifier = Modifier,
  enableClick: Boolean,
) {
  IconView(
    modifier = modifier,
    id = R.drawable.lj_record_holder,
    contentDescription = "LJ Record Holder",
    enableClick = enableClick,
  )
}

@Composable
private fun MapperIcon(
  modifier: Modifier = Modifier,
  enableClick: Boolean,
) {
  IconView(
    modifier = modifier,
    id = R.drawable.mapper,
    contentDescription = "Mapper",
    enableClick = enableClick,
  )
}

@Composable
private fun MovieEditorIcon(
  modifier: Modifier = Modifier,
  enableClick: Boolean,
) {
  IconView(
    modifier = modifier,
    id = R.drawable.movie_maker,
    contentDescription = "Movie Editor",
    enableClick = enableClick,
  )
}

@Composable
private fun IconView(
  @DrawableRes id: Int,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  imageWidth: Dp = 14.dp,
  enableClick: Boolean,
) {
  val context = LocalContext.current
  Image(
    modifier = modifier
      .width(imageWidth)
      .fEnabled(enableClick) {
        clickable {
          Toast
            .makeText(context, contentDescription, Toast.LENGTH_SHORT)
            .show()
        }
      },
    painter = painterResource(id),
    contentDescription = contentDescription,
    contentScale = ContentScale.FillWidth,
  )
}

@Preview
@Composable
private fun PreviewView() {
  ComUserIconsView(
    icons = UserIconsModel(
      isVip = true,
      isRecordHolder = true,
      isLJRecordHolder = true,
      isTournamentRank1 = true,
      isTournamentRank2 = true,
      isTournamentRank3 = true,
      isMapper = true,
      isMovieEditor = true,
    )
  )
}