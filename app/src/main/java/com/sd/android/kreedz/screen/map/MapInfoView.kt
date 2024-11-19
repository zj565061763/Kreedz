package com.sd.android.kreedz.screen.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.UserModel
import com.sd.android.kreedz.feature.common.ui.ComBlurCard
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewSmall

@Composable
fun MapInfoView(
   modifier: Modifier = Modifier,
   mapId: String,
   mapImage: String?,
   mapDate: String?,
   authors: List<UserModel>,
   onClickAuthor: (userId: String) -> Unit,
) {
   Box(
      contentAlignment = Alignment.Center,
      modifier = modifier
         .background(MaterialTheme.colorScheme.surfaceContainer)
         .aspectRatio(1366 / 768f),
   ) {
      MapImageView(
         mapImage = mapImage,
         mapId = mapId,
      )
      MapInfoColumnView(
         mapDate = mapDate,
         authors = authors,
         onClickAuthor = onClickAuthor,
         modifier = Modifier.align(Alignment.TopEnd),
      )
   }
}

@Composable
private fun MapInfoColumnView(
   modifier: Modifier = Modifier,
   mapDate: String?,
   authors: List<UserModel>,
   onClickAuthor: (userId: String) -> Unit,
) {
   Column(
      modifier = modifier.padding(6.dp),
      horizontalAlignment = Alignment.End,
      verticalArrangement = Arrangement.spacedBy(6.dp),
   ) {
      DateView(mapDate = mapDate)
      authors.forEach { item ->
         MapAuthorView(
            author = item,
            onClick = {
               onClickAuthor(item.id)
            }
         )
      }
   }
}

@Composable
private fun MapAuthorView(
   modifier: Modifier = Modifier,
   author: UserModel,
   onClick: () -> Unit,
) {
   ComBlurCard(
      modifier = modifier,
      onClick = onClick,
   ) {
      ComCountryTextViewSmall(
         country = author.country,
         text = author.nickname,
      )
   }
}

@Composable
private fun DateView(
   modifier: Modifier = Modifier,
   mapDate: String?,
) {
   if (mapDate.isNullOrBlank()) return
   ComBlurCard(modifier = modifier) {
      Text(
         text = mapDate,
         fontSize = 12.sp,
         fontWeight = FontWeight.Medium,
      )
   }
}

@Preview
@Composable
private fun PreviewView() {
   AppTheme {
      MapInfoView(
         mapId = "",
         mapImage = "",
         mapDate = "2024-10-10",
         authors = listOf(
            UserModel(
               id = "1",
               nickname = "Chrizzy",
               country = "no",
            ),
            UserModel(
               id = "1",
               nickname = "Chrizzy",
               country = "no",
            ),
         ),
         onClickAuthor = {},
      )
   }
}