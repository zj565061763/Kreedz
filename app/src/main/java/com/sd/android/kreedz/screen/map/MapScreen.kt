package com.sd.android.kreedz.screen.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.data.model.RecordModel
import com.sd.android.kreedz.data.model.UserModel
import com.sd.android.kreedz.feature.common.ui.ComErrorDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
  modifier: Modifier = Modifier,
  mapId: String,
  onClickBack: () -> Unit,
) {
  val vm = viewModel<MapVM>()
  val state by vm.stateFlow.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val context = LocalContext.current

  var showErrorDialog by remember { mutableStateOf(false) }

  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      MapTitleView(
        scrollBehavior = scrollBehavior,
        isLoading = state.isLoading,
        hasError = state.result?.isFailure == true,
        mapName = state.mapName,
        favorite = state.favorite,
        onClickBack = onClickBack,
        onClickError = {
          showErrorDialog = true
        },
        onClickFavorite = {
          vm.clickFavorite()
        },
      )
    },
  ) { padding ->
    BodyView(
      modifier = Modifier.padding(padding),
      mapId = state.mapId,
      mapImage = state.mapImage,
      mapDate = state.mapDate,
      authors = state.authors,
      currentRecord = state.currentRecord,
      records = state.records,
      onClickAuthor = {
        AppRouter.user(context, it)
      },
      onClickPlayer = {
        AppRouter.user(context, it)
      },
    )
  }

  LaunchedEffect(vm, mapId) {
    vm.load(mapId)
  }

  if (showErrorDialog) {
    state.result?.onFailure { error ->
      ComErrorDialog(
        error = error.toString(),
        onDismissRequest = {
          showErrorDialog = false
        },
        onClickRetry = {
          vm.retry()
        },
      )
    }
  }
}

@Composable
private fun BodyView(
  modifier: Modifier = Modifier,
  mapId: String,
  mapImage: String?,
  mapDate: String?,
  authors: List<UserModel>,
  currentRecord: RecordModel?,
  records: List<RecordModel>,
  onClickAuthor: (userId: String) -> Unit,
  onClickPlayer: (userId: String) -> Unit,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(bottom = 24.dp),
  ) {
    mapInfo(
      mapId = mapId,
      mapImage = mapImage,
      mapDate = mapDate,
      authors = authors,
      onClickAuthor = onClickAuthor,
    )

    if (records.isEmpty()) {
      listEmpty()
    } else {
      listRecords(
        currentRecord = currentRecord,
        records = records,
        onClickPlayer = onClickPlayer,
      )
    }
  }
}

private fun LazyListScope.mapInfo(
  mapId: String,
  mapImage: String?,
  mapDate: String?,
  authors: List<UserModel>,
  onClickAuthor: (userId: String) -> Unit,
) {
  item(
    key = "map info",
    contentType = "map info",
  ) {
    MapInfoView(
      mapId = mapId,
      mapImage = mapImage,
      mapDate = mapDate,
      authors = authors,
      onClickAuthor = onClickAuthor,
    )
  }
}

private fun LazyListScope.listRecords(
  currentRecord: RecordModel?,
  records: List<RecordModel>,
  onClickPlayer: (userId: String) -> Unit,
) {
  items(records) { item ->
    Card(
      shape = MaterialTheme.shapes.extraSmall,
      modifier = Modifier.padding(horizontal = 8.dp)
    ) {
      MapRecordItemView(
        record = item,
        currentRecord = currentRecord,
        onClickPlayer = onClickPlayer,
      )
    }
  }
}

private fun LazyListScope.listEmpty() {
  item(
    key = "empty records",
    contentType = "empty records"
  ) {
    Box(
      modifier = Modifier.fillParentMaxWidth(),
      contentAlignment = Alignment.Center,
    ) {
      Text(text = "No records")
    }
  }
}