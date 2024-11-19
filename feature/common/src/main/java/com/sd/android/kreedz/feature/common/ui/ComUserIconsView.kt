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

@Composable
fun ComUserIconsView(
   modifier: Modifier = Modifier,
   icons: UserIconsModel,
) {
   Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(1.dp),
   ) {
      if (icons.isVip) {
         VIPIcon()
      }

      if (icons.isRecordHolder) {
         RecordHolderIcon()
      }

      if (icons.isLJRecordHolder) {
         LJRecordHolderIcon()
      }

      TournamentIcon(
         isTournamentRank1 = icons.isTournamentRank1,
         isTournamentRank2 = icons.isTournamentRank2,
         isTournamentRank3 = icons.isTournamentRank3,
      )

      if (icons.isMapper) {
         MapperIcon()
      }

      if (icons.isMovieEditor) {
         MovieEditorIcon()
      }
   }
}

@Composable
private fun VIPIcon(
   modifier: Modifier = Modifier,
) {
   val context = LocalContext.current
   IconView(
      modifier = modifier.clickable {
         Toast.makeText(context, "VIP", Toast.LENGTH_SHORT).show()
      },
      id = R.drawable.vip,
      contentDescription = "VIP",
   )
}

@Composable
private fun RecordHolderIcon(
   modifier: Modifier = Modifier,
) {
   val context = LocalContext.current
   IconView(
      modifier = modifier.clickable {
         Toast.makeText(context, "Record Holder", Toast.LENGTH_SHORT).show()
      },
      id = R.drawable.record_holder,
      contentDescription = "Record Holder",
   )
}

@Composable
private fun LJRecordHolderIcon(
   modifier: Modifier = Modifier,
) {
   val context = LocalContext.current
   IconView(
      modifier = modifier.clickable {
         Toast.makeText(context, "LJ Record Holder", Toast.LENGTH_SHORT).show()
      },
      id = R.drawable.lj_record_holder,
      contentDescription = "LJ Record Holder",
   )
}

@Composable
private fun TournamentIcon(
   isTournamentRank1: Boolean,
   isTournamentRank2: Boolean,
   isTournamentRank3: Boolean,
) {
   when {
      isTournamentRank1 -> ComTournamentIconView(rank = 1)
      isTournamentRank2 -> ComTournamentIconView(rank = 2)
      isTournamentRank3 -> ComTournamentIconView(rank = 3)
      else -> {}
   }
}

@Composable
private fun MapperIcon(
   modifier: Modifier = Modifier,
) {
   val context = LocalContext.current
   IconView(
      modifier = modifier.clickable {
         Toast.makeText(context, "Mapper", Toast.LENGTH_SHORT).show()
      },
      id = R.drawable.mapper,
      contentDescription = "Mapper",
   )
}

@Composable
private fun MovieEditorIcon(
   modifier: Modifier = Modifier,
) {
   val context = LocalContext.current
   IconView(
      modifier = modifier.clickable {
         Toast.makeText(context, "Movie Editor", Toast.LENGTH_SHORT).show()
      },
      id = R.drawable.movie_maker,
      contentDescription = "Movie Editor",
   )
}

@Composable
private fun IconView(
   @DrawableRes id: Int,
   contentDescription: String?,
   modifier: Modifier = Modifier,
   imageWidth: Dp = 14.dp,
) {
   Image(
      modifier = modifier.width(imageWidth),
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