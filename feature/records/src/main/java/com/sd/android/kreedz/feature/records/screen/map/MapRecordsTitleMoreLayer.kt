package com.sd.android.kreedz.feature.records.screen.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.layer.LayerTarget
import com.sd.lib.compose.layer.TargetAlignment
import com.sd.lib.compose.layer.TargetAlignmentOffset
import com.sd.lib.compose.layer.TargetLayer
import com.sd.lib.compose.layer.relativeAlignment

@Composable
internal fun MapRecordsTitleMoreLayer(
  layerTag: String,
  attach: Boolean,
  onDetachRequest: () -> Unit,
  onClickLongjumps: () -> Unit,
) {
  TargetLayer(
    target = LayerTarget.Tag(layerTag),
    attach = attach,
    onDetachRequest = { onDetachRequest() },
    alignment = TargetAlignment.BottomEnd,
    detachOnTouchBackground = true,
    backgroundColor = Color.Black.copy(0.1f),
    alignmentOffsetX = TargetAlignmentOffset.Target(0.5f).relativeAlignment(),
  ) {
    MoreMenusView(
      onClickLongjumps = onClickLongjumps
    )
  }
}

@Composable
private fun MoreMenusView(
  modifier: Modifier = Modifier,
  onClickLongjumps: () -> Unit,
) {
  Column(
    modifier = modifier.background(
      color = MaterialTheme.colorScheme.surface,
    )
  ) {
    MoreMenusItemView(
      text = "Longjumps",
      onClick = onClickLongjumps,
    )
  }
}

@Composable
private fun MoreMenusItemView(
  modifier: Modifier = Modifier,
  text: String,
  onClick: () -> Unit,
) {
  Box(
    contentAlignment = Alignment.CenterStart,
    modifier = modifier
      .height(48.dp)
      .clickable { onClick() }
      .padding(horizontal = 16.dp),
  ) {
    Text(
      text = text,
      fontSize = 14.sp,
      color = AppTextColor.medium,
    )
  }
}

@Preview
@Composable
private fun PreviewMoreMenusView() {
  AppTheme {
    MoreMenusView(
      onClickLongjumps = {},
    )
  }
}