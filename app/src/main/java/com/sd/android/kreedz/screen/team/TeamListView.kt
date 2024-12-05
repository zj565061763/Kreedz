package com.sd.android.kreedz.screen.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.TeamRoleModel
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewMedium
import com.sd.android.kreedz.feature.common.ui.ComUserIconsView

@Composable
internal fun TeamListView(
  modifier: Modifier = Modifier,
  roles: List<TeamRoleModel>,
  onClickUser: (userId: String) -> Unit,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      top = 16.dp, bottom = 24.dp,
      start = 24.dp, end = 24.dp,
    ),
    verticalArrangement = Arrangement.spacedBy(24.dp),
  ) {
    items(roles) { item ->
      ItemView(
        role = item.roleName,
        users = item.users,
        onClickUser = onClickUser,
      )
    }
  }
}

@Composable
private fun ItemView(
  modifier: Modifier = Modifier,
  role: String,
  users: List<UserWithIconsModel>,
  onClickUser: (userId: String) -> Unit,
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(
      text = role,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
    )

    UsersView(
      users = users,
      onClickUser = onClickUser,
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UsersView(
  modifier: Modifier = Modifier,
  users: List<UserWithIconsModel>,
  onClickUser: (userId: String) -> Unit,
) {
  FlowRow(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    users.forEach { item ->
      UserView(
        user = item,
        onClick = {
          onClickUser(item.id)
        }
      )
    }
  }
}

@Composable
private fun UserView(
  modifier: Modifier = Modifier,
  user: UserWithIconsModel,
  onClick: () -> Unit,
) {
  Card(modifier = modifier) {
    Row(
      modifier = Modifier
        .clickable { onClick() }
        .padding(
          horizontal = 12.dp,
          vertical = 4.dp,
        ),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      ComCountryTextViewMedium(
        country = user.country,
        text = user.nickname,
      )
      ComUserIconsView(icons = user.icons)
    }
  }
}

@Preview
@Composable
private fun PreviewItemView() {
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
    ItemView(
      role = "CTO",
      users = users,
      onClickUser = {},
    )
  }
}