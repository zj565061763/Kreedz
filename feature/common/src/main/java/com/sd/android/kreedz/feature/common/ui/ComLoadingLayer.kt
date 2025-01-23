package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.kmp.compose_layer.Layer
import com.sd.lib.kmp.compose_layer.LayerContainer

@Composable
fun ComLoadingLayer(
  attach: Boolean,
  onDetachRequest: () -> Unit,
) {
  Layer(
    attach = attach,
    onDetachRequest = { onDetachRequest() },
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
    LayerContainer {
      ComLoadingLayer(
        attach = true,
        onDetachRequest = {},
      )
    }
  }
}