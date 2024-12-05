package com.sd.android.kreedz.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.data.model.MainNavigation

@Composable
fun MainNavigationView(
  modifier: Modifier = Modifier,
  listNavigation: List<MainNavigation>,
  selectedNavigationIndex: Int,
  onClickNavigation: (Int) -> Unit,
) {
  NavigationBar(modifier = modifier) {
    listNavigation.forEachIndexed { index, item ->
      val selected = index == selectedNavigationIndex
      ItemView(
        modifier = Modifier.weight(1f),
        text = item.name,
        selected = selected,
        onClick = { onClickNavigation(index) },
      )
    }
  }
}

@Composable
private fun NavigationBar(
  modifier: Modifier = Modifier,
  containerColor: Color = NavigationBarDefaults.containerColor,
  contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
  tonalElevation: Dp = NavigationBarDefaults.Elevation,
  windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
  content: @Composable RowScope.() -> Unit,
) {
  Surface(
    color = containerColor,
    contentColor = contentColor,
    tonalElevation = tonalElevation,
    modifier = modifier
  ) {
    Row(
      modifier =
      Modifier
        .fillMaxWidth()
        .windowInsetsPadding(windowInsets)
        .defaultMinSize(minHeight = 64.dp)
        .selectableGroup(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically,
      content = content
    )
  }
}

@Composable
private fun ItemView(
  modifier: Modifier = Modifier,
  text: String,
  selected: Boolean,
  onClick: () -> Unit,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .selectable(
        selected = selected,
        onClick = onClick,
        enabled = true,
        role = Role.Tab,
        interactionSource = null,
        indication = null,
      ),
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .defaultMinSize(minHeight = 24.dp)
        .background(
          color = if (selected) MaterialTheme.colorScheme.secondaryContainer
          else Color.Transparent,
          shape = CircleShape,
        )
        .padding(horizontal = 12.dp)
    ) {
      Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
      )
    }
  }
}

@Preview
@Composable
private fun PreviewView() {
  AppTheme {
    Box(contentAlignment = Alignment.BottomCenter) {
      MainNavigationView(
        listNavigation = MainNavigation.entries,
        selectedNavigationIndex = 0,
        onClickNavigation = { },
      )
    }
  }
}