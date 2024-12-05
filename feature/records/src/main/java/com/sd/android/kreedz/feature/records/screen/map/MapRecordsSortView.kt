package com.sd.android.kreedz.feature.records.screen.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme

enum class MapRecordsSortType {
  Map,
  Jumper,
  Time,
  Date,
}

data class MapRecordsSortModel(
  val type: MapRecordsSortType,
  val asc: Boolean,
)

@Composable
internal fun MapRecordsSortView(
  modifier: Modifier = Modifier,
  currentSort: MapRecordsSortModel,
  onClickSort: (MapRecordsSortType) -> Unit,
) {
  val sorts = remember { MapRecordsSortType.entries }
  Row(modifier = modifier.fillMaxWidth()) {
    sorts.forEach { item ->
      ItemView(
        modifier = Modifier.weight(1f),
        name = item.displayName(),
        asc = with(currentSort) { if (type == item) asc else null },
        onClick = { onClickSort(item) },
      )
    }
  }
}

@Composable
private fun ItemView(
  modifier: Modifier = Modifier,
  name: String,
  asc: Boolean?,
  onClick: () -> Unit,
) {
  val enabledColor = MaterialTheme.colorScheme.primary
  val disabledColor = MaterialTheme.colorScheme.onSurface.copy(0.3f)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      imageVector = Icons.Default.KeyboardArrowUp,
      contentDescription = "Ascending order",
      tint = if (asc == true) enabledColor else disabledColor,
    )
    Text(
      text = name,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      color = if (asc != null) enabledColor else disabledColor,
    )
    Icon(
      imageVector = Icons.Default.KeyboardArrowDown,
      contentDescription = "Descending order",
      tint = if (asc == false) enabledColor else disabledColor,
    )
  }
}

private fun MapRecordsSortType.displayName(): String {
  return when (this) {
    MapRecordsSortType.Map -> "Map"
    MapRecordsSortType.Jumper -> "Jumper"
    MapRecordsSortType.Time -> "Time"
    MapRecordsSortType.Date -> "Date"
  }
}

@Preview
@Composable
private fun PreviewView() {
  AppTheme {
    MapRecordsSortView(
      currentSort = MapRecordsSortModel(
        type = MapRecordsSortType.Map,
        asc = true,
      ),
      onClickSort = {},
    )
  }
}