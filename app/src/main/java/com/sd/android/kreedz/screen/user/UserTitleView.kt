package com.sd.android.kreedz.screen.user

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sd.android.kreedz.core.ui.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTitleView(
  modifier: Modifier = Modifier,
  nickname: String,
  onClickBack: () -> Unit,
) {
  TopAppBar(
    modifier = modifier,
    title = {
      Text(text = nickname)
    },
    navigationIcon = {
      IconButton(onClick = onClickBack) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors().let {
      it.copy(scrolledContainerColor = it.containerColor)
    },
  )
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    UserTitleView(
      nickname = "topoviygus",
      onClickBack = {},
    )
  }
}