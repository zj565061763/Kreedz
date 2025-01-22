package com.sd.android.kreedz.screen.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.kmp.compose_utils.fClick
import com.sd.lib.kmp.compose_utils.fEnabled

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTitleView(
  modifier: Modifier = Modifier,
  scrollBehavior: TopAppBarScrollBehavior?,
  isLoading: Boolean,
  hasError: Boolean,
  mapName: String,
  favorite: Boolean,
  onClickBack: () -> Unit,
  onClickError: () -> Unit,
  onClickFavorite: () -> Unit,
) {
  TopAppBar(
    modifier = modifier,
    scrollBehavior = scrollBehavior,
    colors = TopAppBarDefaults.topAppBarColors().let {
      it.copy(scrolledContainerColor = it.containerColor)
    },
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fEnabled(!isLoading && hasError) {
          fClick { onClickError() }
        }
      ) {
        Text(
          text = mapName,
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold,
        )
        LoadingStateView(
          isLoading = isLoading,
          hasError = hasError,
        )
      }
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
      IconButton(onClick = onClickFavorite) {
        Icon(
          imageVector = if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
          contentDescription = if (favorite) "Remove favorite" else "Add favorite",
        )
      }
    }
  )
}

@Composable
private fun LoadingStateView(
  modifier: Modifier = Modifier,
  isLoading: Boolean,
  hasError: Boolean,
) {
  when {
    isLoading -> {
      CircularProgressIndicator(
        strokeWidth = 1.dp,
        modifier = modifier
          .size(12.dp)
          .offset(y = 1.dp),
      )
    }

    hasError -> {
      Icon(
        imageVector = Icons.Outlined.Warning,
        contentDescription = "Load map error",
        tint = MaterialTheme.colorScheme.error,
        modifier = modifier.size(12.dp),
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewView() {
  AppTheme {
    MapTitleView(
      scrollBehavior = null,
      isLoading = false,
      hasError = true,
      mapName = "bkz_goldbhop",
      favorite = true,
      onClickBack = {},
      onClickError = {},
      onClickFavorite = {},
    )
  }
}