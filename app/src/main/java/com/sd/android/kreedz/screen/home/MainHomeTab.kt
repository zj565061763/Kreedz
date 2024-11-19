package com.sd.android.kreedz.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme

enum class MainHomeTab {
   News,
   Release,
   Ranking,
   Servers,
   Blog,
   Team,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeTabView(
   modifier: Modifier = Modifier,
   tabs: List<MainHomeTab>,
   selectedTabIndex: Int,
   onClickTab: (Int) -> Unit,
) {
   PrimaryScrollableTabRow(
      modifier = modifier,
      selectedTabIndex = selectedTabIndex,
      containerColor = Color.Transparent,
      edgePadding = 0.dp,
      divider = {},
      indicator = {
         if (selectedTabIndex in tabs.indices) {
            TabRowDefaults.PrimaryIndicator(
               Modifier.tabIndicatorOffset(selectedTabIndex),
            )
         }
      },
   ) {
      tabs.forEachIndexed { index, item ->
         Box(
            modifier = Modifier
               .height(40.dp)
               .clickable { onClickTab(index) }
               .padding(horizontal = 8.dp)
         ) {
            Text(
               text = item.name,
               fontSize = 18.sp,
               fontWeight = FontWeight.Medium,
               modifier = Modifier.align(Alignment.Center),
            )
         }
      }
   }
}

@Preview
@Composable
private fun PreviewTabsView() {
   AppTheme {
      MainHomeTabView(
         tabs = MainHomeTab.entries,
         selectedTabIndex = 0,
         onClickTab = {},
      )
   }
}