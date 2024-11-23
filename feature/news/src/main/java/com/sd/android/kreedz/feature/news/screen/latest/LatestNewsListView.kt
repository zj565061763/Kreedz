package com.sd.android.kreedz.feature.news.screen.latest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.NewsModel
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewSmall
import com.sd.android.kreedz.feature.common.ui.ComUserIconsView
import com.sd.lib.compose.paging.fPagingAppend
import com.sd.lib.compose.paging.fPagingItems

@Composable
internal fun LatestNewsListView(
   modifier: Modifier = Modifier,
   lazyListState: LazyListState,
   news: LazyPagingItems<NewsModel>,
   onClickNews: (NewsModel) -> Unit,
   onClickOpenUri: (NewsModel) -> Unit,
) {
   LazyColumn(
      modifier = modifier.fillMaxSize(),
      state = lazyListState,
      contentPadding = PaddingValues(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
   ) {
      fPagingItems(
         items = news,
         key = news.itemKey { it.id },
      ) { _, item ->
         Card(shape = MaterialTheme.shapes.extraSmall) {
            ItemView(
               title = item.title,
               date = item.dateStr,
               author = item.author,
               onClickOpenUri = {
                  onClickOpenUri(item)
               },
               modifier = Modifier.clickable {
                  onClickNews(item)
               },
            )
         }
      }

      fPagingAppend(
         items = news,
      )
   }
}

@Composable
private fun ItemView(
   modifier: Modifier = Modifier,
   title: String,
   date: String,
   author: UserWithIconsModel,
   onClickOpenUri: () -> Unit,
) {
   ConstraintLayout(
      modifier = modifier
         .fillMaxWidth()
         .padding(8.dp)
   ) {
      val (
         refTitle, refOpenUri,
         refAuthor, refAuthorIcons, refDate,
      ) = createRefs()

      // title
      Text(
         text = title,
         fontSize = 14.sp,
         fontWeight = FontWeight.Medium,
         lineHeight = 18.sp,
         modifier = Modifier.constrainAs(refTitle) {
            top.linkTo(parent.top)
            linkTo(
               start = parent.start,
               end = refOpenUri.start,
               bias = 0f,
            )
            width = Dimension.fillToConstraints
         }
      )

      ComCountryTextViewSmall(
         country = author.country,
         text = author.nickname,
         modifier = Modifier.constrainAs(refAuthor) {
            start.linkTo(parent.start)
            top.linkTo(refTitle.bottom, 6.dp)
         }
      )
      ComUserIconsView(
         icons = author.icons,
         enableClick = false,
         modifier = Modifier.constrainAs(refAuthorIcons) {
            centerVerticallyTo(refAuthor)
            start.linkTo(refAuthor.end, 4.dp)
         }
      )

      // Date
      Text(
         text = date,
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier.constrainAs(refDate) {
            end.linkTo(parent.end)
            top.linkTo(refTitle.bottom, 6.dp)
         }
      )

      // Open uri
      Icon(
         imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
         contentDescription = "Open uri",
         modifier = Modifier
            .constrainAs(refOpenUri) {
               centerVerticallyTo(refTitle)
               end.linkTo(parent.end)
            }
            .clickable { onClickOpenUri() }
      )
   }
}

@Preview
@Composable
private fun PreviewView() {
   AppTheme {
      Card(shape = MaterialTheme.shapes.extraSmall) {
         ItemView(
            title = "WR Release #819 - 22 new world records + News",
            date = "30/09/2024 18:20",
            author = UserWithIconsModel(
               id = "1",
               nickname = "phayzeeVW",
               country = "ro",
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
            ),
            onClickOpenUri = {},
         )
      }
   }
}