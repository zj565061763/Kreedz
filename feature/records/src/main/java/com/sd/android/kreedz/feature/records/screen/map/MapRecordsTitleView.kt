package com.sd.android.kreedz.feature.records.screen.map

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.layer.layerTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MapRecordsTitleView(
  modifier: Modifier = Modifier,
  scrollBehavior: TopAppBarScrollBehavior?,
  textFieldState: TextFieldState,
  onClickIconSort: () -> Unit,
  onClickLongjumps: () -> Unit,
) {
  var showMoreMenusLayer by remember { mutableStateOf(false) }

  TopAppBar(
    modifier = modifier,
    colors = TopAppBarDefaults.topAppBarColors().let {
      it.copy(scrolledContainerColor = it.containerColor)
    },
    title = {
      TitleView(
        textFieldState = textFieldState,
        onClickIconMore = { showMoreMenusLayer = true },
        onClickIconSort = onClickIconSort,
      )
    },
    scrollBehavior = scrollBehavior,
  )

  MapRecordsTitleMoreLayer(
    layerTag = MORE_MENU_TAG,
    attach = showMoreMenusLayer,
    onDetachRequest = { showMoreMenusLayer = false },
    onClickLongjumps = {
      showMoreMenusLayer = false
      onClickLongjumps()
    }
  )
}

@Composable
private fun TitleView(
  modifier: Modifier = Modifier,
  textFieldState: TextFieldState,
  onClickIconSort: () -> Unit,
  onClickIconMore: () -> Unit,
) {
  ConstraintLayout(
    modifier = modifier.fillMaxWidth()
  ) {
    val (refSearch, refIcons) = createRefs()

    // search
    MapRecordsTitleSearchView(
      textFieldState = textFieldState,
      modifier = Modifier.constrainAs(refSearch) {
        centerVerticallyTo(parent)
        start.linkTo(parent.start)
        width = Dimension.percent(0.6f)
      }
    )

    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.constrainAs(refIcons) {
        centerVerticallyTo(parent)
        end.linkTo(parent.end)
      }
    ) {
      // sort
      IconButton(onClick = onClickIconSort) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.List,
          contentDescription = null,
        )
      }

      // more
      IconButton(
        onClick = onClickIconMore,
        modifier = Modifier.layerTag(MORE_MENU_TAG),
      ) {
        Icon(
          imageVector = Icons.Default.MoreVert,
          contentDescription = null,
        )
      }
    }
  }
}

private const val MORE_MENU_TAG = "main_records_more_menu_tag"

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewTitleView() {
  AppTheme {
    MapRecordsTitleView(
      scrollBehavior = null,
      textFieldState = TextFieldState("666666"),
      onClickIconSort = {},
      onClickLongjumps = {},
    )
  }
}