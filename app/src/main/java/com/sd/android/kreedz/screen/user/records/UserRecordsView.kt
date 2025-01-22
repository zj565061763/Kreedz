package com.sd.android.kreedz.screen.user.records

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.UserRecordModel
import com.sd.lib.kmp.compose_pager.FOnSettledPage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRecordsView(
  modifier: Modifier = Modifier,
  onDismissRequest: () -> Unit,
  currentRecords: List<UserRecordModel>,
  previousRecords: List<UserRecordModel>,
  onClickItem: (mapId: String) -> Unit,
) {
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  val tabs = remember {
    UserRecordsTab.entries.map {
      UserRecordsTabModel(tab = it)
    }
  }

  val scope = rememberCoroutineScope()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val pagerState = rememberPagerState { tabs.size }
  pagerState.FOnSettledPage { selectedTabIndex = it }

  ModalBottomSheet(
    modifier = modifier.fillMaxSize(),
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
  ) {
    TabView(
      modifier = Modifier.fillMaxWidth(),
      tabs = tabs,
      selectedTabIndex = selectedTabIndex,
      onClickTab = {
        scope.launch {
          pagerState.animateScrollToPage(it)
        }
      },
    )
    HorizontalPager(
      state = pagerState,
      beyondViewportPageCount = tabs.size,
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
    ) { index ->
      val records = when (tabs[index].tab) {
        UserRecordsTab.Current -> currentRecords
        UserRecordsTab.Previous -> previousRecords
      }
      UserRecordsListView(
        records = records,
        onClickItem = {
          it.mapId?.also { mapId ->
            onClickItem(mapId)
          }
        },
      )
    }
  }

  val numberOfCurrentRecords = currentRecords.size
  val numberOfPreviousRecords = previousRecords.size
  LaunchedEffect(
    tabs,
    numberOfCurrentRecords,
    numberOfPreviousRecords,
  ) {
    tabs.forEach { item ->
      when (item.tab) {
        UserRecordsTab.Current -> item.number = numberOfCurrentRecords.toString()
        UserRecordsTab.Previous -> item.number = numberOfPreviousRecords.toString()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabView(
  modifier: Modifier = Modifier,
  tabs: List<UserRecordsTabModel>,
  selectedTabIndex: Int,
  onClickTab: (Int) -> Unit,
) {
  PrimaryTabRow(
    modifier = modifier.fillMaxWidth(),
    selectedTabIndex = selectedTabIndex,
    containerColor = Color.Transparent,
    divider = {},
    indicator = {
      TabRowDefaults.PrimaryIndicator(
        modifier = Modifier.tabIndicatorOffset(selectedTabIndex),
        width = 36.dp,
      )
    },
  ) {
    tabs.forEachIndexed { index, item ->
      Tab(
        selected = index == selectedTabIndex,
        onClick = { onClickTab(index) },
      ) {
        TabItemView(
          title = item.title(),
          number = item.number,
        )
      }
    }
  }
}

@Composable
private fun TabItemView(
  modifier: Modifier = Modifier,
  title: String,
  number: String,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .heightIn(48.dp),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = "$title ($number)",
      fontSize = 14.sp,
      fontWeight = FontWeight.Medium,
    )
  }
}

private enum class UserRecordsTab {
  Current,
  Previous,
}

@Stable
private data class UserRecordsTabModel(
  val tab: UserRecordsTab,
) {
  var number: String by mutableStateOf("0")

  fun title(): String {
    return when (tab) {
      UserRecordsTab.Current -> "Current Records"
      UserRecordsTab.Previous -> "Previous Records"
    }
  }
}

@Preview
@Composable
private fun PreviewTabView() {
  val tabs = listOf(
    UserRecordsTabModel(
      tab = UserRecordsTab.Current,
    ).apply { number = "171" },
    UserRecordsTabModel(
      tab = UserRecordsTab.Previous,
    ).apply { number = "264" }
  )

  AppTheme {
    TabView(
      tabs = tabs,
      selectedTabIndex = 0,
      onClickTab = { },
    )
  }
}