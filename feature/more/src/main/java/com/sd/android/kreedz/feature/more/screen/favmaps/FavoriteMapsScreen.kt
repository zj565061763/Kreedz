package com.sd.android.kreedz.feature.more.screen.favmaps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.data.model.MapRecordModel
import com.sd.android.kreedz.data.model.UserModel
import com.sd.android.kreedz.feature.common.ui.ComErrorView
import com.sd.android.kreedz.feature.common.ui.ComMapRecordsItemView
import com.sd.android.kreedz.feature.common.ui.ComResultBox
import com.sd.android.kreedz.feature.common.ui.ComTextLabelView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoriteMapsScreen(
  modifier: Modifier = Modifier,
  vm: FavoriteMapsVM = viewModel(),
  onClickBack: () -> Unit,
) {
  val state by vm.stateFlow.collectAsStateWithLifecycle()
  val context = LocalContext.current

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text(text = "Favorite Maps") },
        navigationIcon = {
          IconButton(onClick = onClickBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
            )
          }
        },
      )
    },
  ) { padding ->
    ComResultBox(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      isLoading = state.isLoading,
      result = state.result,
      onFailure = {
        ComErrorView(
          error = it,
          onClickRetry = { vm.retry() }
        )
      },
    ) {
      BodyView(
        maps = state.maps,
        onClickItem = {
          AppRouter.map(context, it.map.id)
        },
        onClickPlayer = {
          AppRouter.user(context, it.id)
        }
      )
    }
  }

  LaunchedEffect(vm) {
    vm.load()
  }
}

@Composable
private fun BodyView(
  modifier: Modifier = Modifier,
  maps: List<MapRecordModel>,
  onClickItem: (MapRecordModel) -> Unit,
  onClickPlayer: (UserModel) -> Unit,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(8.dp),
  ) {
    item(
      key = "items count",
      contentType = "items count",
    ) {
      Box(modifier = Modifier.fillParentMaxWidth()) {
        ComTextLabelView(
          text = maps.size.toString(),
          label = "items",
          textColor = AppTextColor.small,
          textFontSize = 12.sp,
          modifier = Modifier.align(Alignment.Center),
        )
      }
    }

    items(
      items = maps,
      key = { it.map.id },
    ) { item ->
      Card(
        shape = MaterialTheme.shapes.extraSmall,
        onClick = { onClickItem(item) }
      ) {
        ComMapRecordsItemView(
          item = item,
          keywords = emptyList(),
          onClickPlayer = {
            item.record?.player?.also { player ->
              onClickPlayer(player)
            }
          }
        )
      }
    }
  }
}