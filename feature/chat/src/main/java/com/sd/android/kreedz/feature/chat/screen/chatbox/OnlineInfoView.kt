package com.sd.android.kreedz.feature.chat.screen.chatbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel

@Composable
internal fun OnlineInfoView(
   modifier: Modifier = Modifier,
   guestsCount: Int,
   onlineUsers: List<UserWithIconsModel>,
   onClickOnlineUser: (userId: String) -> Unit,
) {
   ConstraintLayout(modifier = modifier.fillMaxSize()) {
      val (refCount, refUsers) = createRefs()

      OnlineCountView(
         guestsCount = guestsCount,
         usersCount = onlineUsers.size,
         modifier = Modifier.constrainAs(refCount) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
         }
      )

      OnlineUsersView(
         users = onlineUsers,
         onClickUser = onClickOnlineUser,
         modifier = Modifier.constrainAs(refUsers) {
            width = Dimension.matchParent
            bottom.linkTo(parent.bottom, 4.dp)
         }
      )
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
      OnlineInfoView(
         modifier = Modifier.height(48.dp),
         guestsCount = 4,
         onlineUsers = users,
         onClickOnlineUser = {},
      )
   }
}