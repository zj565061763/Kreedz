package com.sd.android.kreedz.feature.records.screen.lj

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.layer.Directions
import com.sd.lib.compose.layer.LayerTarget
import com.sd.lib.compose.layer.TargetAlignment
import com.sd.lib.compose.layer.TargetLayer
import com.sd.lib.compose.layer.layerTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LJRecordsTitleView(
   modifier: Modifier = Modifier,
   scrollBehavior: TopAppBarScrollBehavior?,
   onClickBack: () -> Unit,
   groups: List<String>,
   onClickGroup: (String) -> Unit,
) {
   var showExpandedLayer by remember { mutableStateOf(false) }

   TopAppBar(
      modifier = modifier.layerTag(EXPAND_TAB_TAG),
      colors = TopAppBarDefaults.topAppBarColors().let {
         it.copy(scrolledContainerColor = it.containerColor)
      },
      title = {
         Text(text = "Longjumps")
      },
      navigationIcon = {
         IconButton(onClick = onClickBack) {
            Icon(
               imageVector = Icons.AutoMirrored.Filled.ArrowBack,
               contentDescription = "Back",
            )
         }
      },
      actions = {
         if (groups.isNotEmpty()) {
            IconButton(onClick = {
               showExpandedLayer = !showExpandedLayer
            }) {
               Icon(
                  imageVector = Icons.Default.Menu,
                  contentDescription = "Menu",
               )
            }
         }
      },
      scrollBehavior = scrollBehavior,
   )

   TargetLayer(
      target = LayerTarget.Tag(EXPAND_TAB_TAG),
      attach = showExpandedLayer,
      onDetachRequest = { showExpandedLayer = false },
      alignment = TargetAlignment.BottomCenter,
      detachOnTouchBackground = true,
      backgroundColor = Color.Black.copy(0.1f),
      clipBackgroundDirection = Directions.Top,
   ) {
      ExpandedView(
         groups = groups,
         onClickGroup = {
            showExpandedLayer = false
            onClickGroup(it)
         }
      )
   }
}

@Composable
private fun ExpandedView(
   modifier: Modifier = Modifier,
   groups: List<String>,
   onClickGroup: (String) -> Unit,
) {
   LazyColumn(
      modifier = modifier
         .fillMaxWidth()
         .background(MaterialTheme.colorScheme.surface)
   ) {
      items(groups) { item ->
         ExpandedItemView(
            modifier = Modifier.fillMaxWidth(),
            text = item,
            onClick = { onClickGroup(item) }
         )
      }
   }
}

@Composable
private fun ExpandedItemView(
   modifier: Modifier = Modifier,
   text: String,
   onClick: () -> Unit,
) {
   Box(
      modifier = modifier
         .height(48.dp)
         .clickable { onClick() }
         .padding(horizontal = 16.dp),
      contentAlignment = Alignment.Center,
   ) {
      Text(
         text = text,
         fontSize = 14.sp,
      )
   }
}

private const val EXPAND_TAB_TAG = "longjumps_expand_tab_tag"

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewTitleView() {
   AppTheme {
      LJRecordsTitleView(
         scrollBehavior = null,
         onClickBack = {},
         groups = listOf("LongJump", "CountJump", "StandUp CountJump"),
         onClickGroup = {},
      )
   }
}

@Preview
@Composable
private fun PreviewExpandedView() {
   AppTheme {
      ExpandedView(
         groups = listOf("LongJump", "CountJump", "StandUp CountJump"),
         onClickGroup = { },
      )
   }
}