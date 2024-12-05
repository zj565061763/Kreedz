package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.sd.android.kreedz.core.ui.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComLoadingDialog(
  onDismissRequest: () -> Unit,
) {
  BasicAlertDialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(
      dismissOnClickOutside = false,
      usePlatformDefaultWidth = false,
    ),
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      CircularProgressIndicator()
    }
  }
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    ComLoadingDialog(
      onDismissRequest = {},
    )
  }
}