package com.sd.android.kreedz.screen.more

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.feature.common.ui.ComBrightnessModeView
import com.sd.android.kreedz.feature.more.R
import com.sd.lib.compose.utils.fClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMoreScreen(
  modifier: Modifier = Modifier,
  vm: MainMoreVM = viewModel(),
) {
  val state by vm.stateFlow.collectAsStateWithLifecycle()
  val context = LocalContext.current

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        expandedHeight = 48.dp,
        title = {},
        navigationIcon = {
          ToggleLightModeButton()
        },
      )
    },
  ) { padding ->
    AppPullToRefresh(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      isRefreshing = state.isLoading,
      onRefresh = { vm.refresh() },
      enabled = !state.userId.isNullOrBlank(),
    ) {
      BodyView(
        userId = state.userId,
        nickname = state.nickname,
        country = state.country,
        countryName = state.countryName,
        roles = state.roles,
        icons = state.icons,
        versionName = state.versionName,
        onClickUserInfo = {
          val userId = state.userId
          if (userId.isNullOrBlank()) {
            AppRouter.login(context)
          } else {
            AppRouter.user(context, userId)
          }
        },
        onClickFavoriteMaps = {
          AppRouter.favoriteMaps(context)
        },
        onClickLogout = {
          vm.logout()
        },
      )
    }
  }
}

@Composable
private fun BodyView(
  modifier: Modifier = Modifier,
  userId: String?,
  nickname: String?,
  country: String?,
  countryName: String?,
  roles: List<String>,
  icons: UserIconsModel,
  versionName: String,
  onClickUserInfo: () -> Unit,
  onClickFavoriteMaps: () -> Unit,
  onClickLogout: () -> Unit,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    MainMoreUserInfoView(
      nickname = nickname,
      country = country,
      countryName = countryName,
      roles = roles,
      icons = icons,
      modifier = Modifier.fClick {
        onClickUserInfo()
      },
    )

    Spacer(Modifier.height(24.dp))
    MainMoreLinksView()

    Spacer(Modifier.height(24.dp))
    MainMoreItemsView(
      onClickFavoriteMaps = onClickFavoriteMaps,
      modifier = Modifier.padding(horizontal = 12.dp),
    )

    Spacer(Modifier.weight(1f))

    LogoutButton(
      userId = userId,
      onClickLogout = onClickLogout,
      modifier = Modifier.padding(bottom = 16.dp)
    )
    Text(
      text = versionName,
      fontSize = 12.sp,
      color = AppTextColor.small,
      modifier = Modifier.padding(bottom = 4.dp)
    )
  }
}

@Composable
private fun LogoutButton(
  modifier: Modifier = Modifier,
  userId: String?,
  onClickLogout: () -> Unit,
) {
  if (userId.isNullOrBlank()) return
  var showConfirmDialog by remember { mutableStateOf(false) }

  OutlinedButton(
    onClick = { showConfirmDialog = true },
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
    modifier = modifier.width(160.dp),
  ) {
    Text(text = "Logout")
  }

  if (showConfirmDialog) {
    AlertDialog(
      shape = MaterialTheme.shapes.medium,
      properties = DialogProperties(
        dismissOnClickOutside = false,
      ),
      onDismissRequest = {
        showConfirmDialog = false
      },
      confirmButton = {
        TextButton(
          onClick = {
            showConfirmDialog = false
            onClickLogout()
          }
        ) {
          Text(text = "Logout")
        }
      },
      dismissButton = {
        TextButton(
          onClick = { showConfirmDialog = false }
        ) {
          Text(text = "Cancel")
        }
      },
      text = {
        Text(text = "Would you like to logout?")
      },
    )
  }
}

@Composable
private fun ToggleLightModeButton(
  modifier: Modifier = Modifier,
) {
  ComBrightnessModeView {
    IconButton(
      modifier = modifier,
      onClick = { toggleMode() }
    ) {
      Icon(
        painter = painterResource(
          if (isLightMode) R.drawable.brightness_light_mode
          else R.drawable.brightness_dark_mode
        ),
        contentDescription = if (isLightMode) "Light mode" else "Dark mode",
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
    BodyView(
      userId = "1",
      nickname = "topoviygus",
      country = "ru",
      countryName = "Russia",
      roles = roles,
      versionName = "1.0.0",
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
      onClickUserInfo = {},
      onClickFavoriteMaps = {},
      onClickLogout = {},
    )
  }
}