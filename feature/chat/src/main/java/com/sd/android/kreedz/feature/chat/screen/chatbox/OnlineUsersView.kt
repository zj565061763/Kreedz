package com.sd.android.kreedz.feature.chat.screen.chatbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewSmall
import com.sd.android.kreedz.feature.common.ui.ComUserIconsView

@Composable
internal fun OnlineUsersView(
  modifier: Modifier = Modifier,
  users: List<UserWithIconsModel>,
  onClickUser: (userId: String) -> Unit,
) {
  LazyRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(
      items = users,
      key = { it.id },
    ) { item ->
      ItemView(
        user = item,
        onClick = {
          onClickUser(item.id)
        }
      )
    }
  }
}

@Composable
private fun ItemView(
  modifier: Modifier = Modifier,
  user: UserWithIconsModel,
  onClick: () -> Unit,
) {
  Card(modifier = modifier) {
    Row(
      modifier = Modifier
        .clickable { onClick() }
        .padding(
          horizontal = 8.dp,
          vertical = 2.dp,
        ),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      ComCountryTextViewSmall(
        country = user.country,
        text = user.nickname,
      )
      ComUserIconsView(icons = user.icons)
    }
  }
}

@Preview
@Composable
private fun Preview() {
  val users = listOf(
    UserWithIconsModel(
      id = "1",
      nickname = "g-Lp",
      country = "fr",
      icons = UserIconsModel.Default.copy(
        isRecordHolder = true,
        isTournamentRank1 = true,
      )
    ),
    UserWithIconsModel(
      id = "2",
      nickname = "PeKz^",
      country = "de",
      icons = UserIconsModel.Default,
    ),
    UserWithIconsModel(
      id = "3",
      nickname = "sitka",
      country = "se",
      icons = UserIconsModel.Default.copy(
        isVip = true,
      ),
    )
  )
  AppTheme {
    OnlineUsersView(
      users = users,
      onClickUser = {},
    )
  }
}