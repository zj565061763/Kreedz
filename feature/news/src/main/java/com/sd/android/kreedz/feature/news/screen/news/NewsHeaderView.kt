package com.sd.android.kreedz.feature.news.screen.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewSmall
import com.sd.android.kreedz.feature.common.ui.ComHtmlView
import com.sd.android.kreedz.feature.common.ui.ComUserIconsView

@Composable
internal fun NewsHeaderView(
   modifier: Modifier = Modifier,
   title: String,
   html: String,
   authorCountry: String?,
   authorNickname: String?,
   authorIcons: UserIconsModel?,
   dateStr: String,
   onClickAuthor: () -> Unit,
) {
   Column(
      modifier = modifier
         .fillMaxWidth()
         .padding(8.dp),
   ) {
      Text(
         text = title,
         fontSize = 24.sp,
         fontWeight = FontWeight.Medium,
      )

      if (authorNickname != null) {
         Spacer(Modifier.height(8.dp))
         AuthorInfoView(
            authorCountry = authorCountry,
            authorNickname = authorNickname,
            authorIcons = authorIcons,
            dateStr = dateStr,
            onClickAuthor = onClickAuthor,
         )
      }

      Spacer(Modifier.height(16.dp))
      ComHtmlView(
         modifier = modifier,
         html = html,
      )
   }
}

@Composable
private fun AuthorInfoView(
   modifier: Modifier = Modifier,
   authorCountry: String?,
   authorNickname: String?,
   authorIcons: UserIconsModel?,
   dateStr: String,
   onClickAuthor: () -> Unit,
) {
   Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
   ) {
      Text(
         text = "Written by",
         fontSize = 12.sp,
         modifier = Modifier.alignByBaseline(),
      )

      Spacer(Modifier.width(4.dp))
      ComCountryTextViewSmall(
         country = authorCountry,
         text = authorNickname,
         modifier = Modifier
            .alignByBaseline()
            .clickable { onClickAuthor() },
      )

      if (authorIcons != null) {
         Spacer(Modifier.width(4.dp))
         ComUserIconsView(
            icons = authorIcons,
         )
      }

      Spacer(Modifier.weight(1f))
      Text(
         text = dateStr,
         fontSize = 12.sp,
         modifier = Modifier.alignByBaseline(),
      )
   }
}

@Preview
@Composable
private fun Preview() {
   NewsHeaderView(
      title = "WR Release #820 - 23 NEW WORLD RECORDS",
      html = "",
      authorCountry = "ro",
      authorNickname = "phayzeeVW",
      authorIcons = UserIconsModel.Default.copy(
         isLJRecordHolder = true,
      ),
      dateStr = "28/10/2024 14:27",
      onClickAuthor = {},
   )
}