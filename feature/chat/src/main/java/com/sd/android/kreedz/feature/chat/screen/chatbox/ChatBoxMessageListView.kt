package com.sd.android.kreedz.feature.chat.screen.chatbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.data.model.ChatBoxDateModel
import com.sd.android.kreedz.data.model.ChatBoxItemModel
import com.sd.android.kreedz.data.model.ChatBoxMessageModel
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewLarge
import com.sd.android.kreedz.feature.common.ui.ComUserIconsView
import com.sd.android.kreedz.feature.common.ui.comAnnotatedLink
import com.sd.lib.kmp.compose_utils.fClick
import com.sd.lib.kmp.paging.compose.PagingPresenter
import com.sd.lib.kmp.paging.compose.pagingItemAppend
import com.sd.lib.kmp.paging.compose.pagingItems

@Composable
internal fun ChatBoxMessageListView(
  modifier: Modifier = Modifier,
  lazyListState: LazyListState,
  paging: PagingPresenter<ChatBoxItemModel>,
  onClickAuthor: (userId: String) -> Unit,
  onClickMessage: (ChatBoxMessageModel) -> Unit,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    state = lazyListState,
    contentPadding = PaddingValues(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    pagingItems(
      paging = paging,
      contentType = {
        when (it) {
          is ChatBoxMessageModel -> "message"
          is ChatBoxDateModel -> "date"
        }
      }
    ) { item ->
      when (item) {
        is ChatBoxMessageModel -> {
          Card(shape = MaterialTheme.shapes.extraSmall) {
            ItemView(
              country = item.author.country,
              countryText = item.author.nickname,
              dateTime = item.dateTimeStr,
              message = item.message,
              icons = item.author.icons,
              onClickAuthor = {
                onClickAuthor(item.author.id)
              },
              modifier = Modifier.fClick {
                onClickMessage(item)
              }
            )
          }
        }

        is ChatBoxDateModel -> {
          Text(
            text = item.dateStr,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(
              horizontal = 8.dp,
              vertical = 4.dp,
            ),
          )
        }
      }
    }

    pagingItemAppend(paging)
  }
}

@Composable
private fun ItemView(
  modifier: Modifier = Modifier,
  country: String?,
  countryText: String?,
  dateTime: String,
  message: String,
  icons: UserIconsModel,
  onClickAuthor: () -> Unit,
) {
  ConstraintLayout(
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp)
  ) {
    val (refUser, refDateTime, refMessage, refIcons) = createRefs()

    ComCountryTextViewLarge(
      country = country,
      text = countryText,
      modifier = Modifier
        .constrainAs(refUser) {
          top.linkTo(parent.top)
          start.linkTo(parent.start)
        }
        .clickable { onClickAuthor() }
    )

    Text(
      text = dateTime,
      fontSize = 12.sp,
      color = AppTextColor.medium,
      modifier = Modifier.constrainAs(refDateTime) {
        top.linkTo(parent.top)
        end.linkTo(parent.end)
      }
    )

    Box(
      modifier = Modifier.constrainAs(refMessage) {
        top.linkTo(refUser.bottom, 6.dp)
        start.linkTo(refUser.start)
      }
    ) {
      SelectionContainer {
        Text(
          text = message.comAnnotatedLink(),
          fontSize = 14.sp,
          color = AppTextColor.medium,
        )
      }
    }

    ComUserIconsView(
      icons = icons,
      modifier = Modifier.constrainAs(refIcons) {
        centerVerticallyTo(refUser)
        start.linkTo(refUser.end, 2.dp)
      }
    )
  }
}

@Preview
@Composable
private fun PreviewItemView() {
  ItemView(
    country = "cn",
    countryText = "zhengjun",
    dateTime = "10/10/2024",
    message = "Welcome to kreedz https://www.youtube.com",
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
    onClickAuthor = {},
  )
}