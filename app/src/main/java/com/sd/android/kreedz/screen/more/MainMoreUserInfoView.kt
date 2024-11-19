package com.sd.android.kreedz.screen.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewLarge
import com.sd.android.kreedz.feature.common.ui.ComUserIconsView
import com.sd.android.kreedz.feature.more.R

@Composable
fun MainMoreUserInfoView(
   modifier: Modifier = Modifier,
   nickname: String?,
   country: String?,
   countryName: String?,
   roles: List<String>,
   icons: UserIconsModel,
) {
   Column(
      modifier = modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
   ) {
      Image(
         painterResource(R.drawable.avatar),
         contentDescription = "Avatar",
         modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
      )

      NicknameView(
         nickname = nickname,
         icons = icons,
         modifier = Modifier.padding(top = 8.dp),
      )

      ComCountryTextViewLarge(
         country = country,
         text = countryName,
         modifier = Modifier.padding(top = 6.dp),
      )

      RolesView(
         roles = roles,
         modifier = Modifier.padding(top = 6.dp),
      )
   }
}

@Composable
private fun NicknameView(
   modifier: Modifier = Modifier,
   nickname: String?,
   icons: UserIconsModel,
) {
   Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(2.dp),
   ) {
      Text(
         text = nickname ?: "",
         fontSize = 16.sp,
         fontWeight = FontWeight.Medium,
      )
      ComUserIconsView(
         icons = icons,
      )
   }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RolesView(
   modifier: Modifier = Modifier,
   roles: List<String>,
) {
   FlowRow(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp),
   ) {
      roles.forEach { item ->
         Text(
            text = "( $item )",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
         )
      }
   }
}

@Preview
@Composable
private fun Preview() {
   val roles = listOf(
      "RECORD_HOLDER",
      "LJ_RECORD_HOLDER",
      "CUP_WINNER_GOLD",
   )
   AppTheme {
      MainMoreUserInfoView(
         nickname = "topoviygus",
         country = "ru",
         countryName = "Russia",
         roles = roles,
         icons = UserIconsModel(
            isVip = true,
            isRecordHolder = true,
            isLJRecordHolder = true,
            isTournamentRank1 = true,
            isTournamentRank2 = true,
            isTournamentRank3 = true,
            isMapper = true,
            isMovieEditor = true,
         ),
      )
   }
}

@Preview
@Composable
private fun PreviewEmpty() {
   AppTheme {
      MainMoreUserInfoView(
         nickname = null,
         country = null,
         countryName = null,
         roles = emptyList(),
         icons = UserIconsModel.Default,
      )
   }
}