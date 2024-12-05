package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ComResultBox(
  modifier: Modifier = Modifier,
  isLoading: Boolean,
  result: Result<*>?,
  onFailure: @Composable (Throwable) -> Unit,
  onSuccess: @Composable () -> Unit,
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    if (isLoading) {
      CircularProgressIndicator()
    } else {
      result?.onFailure {
        onFailure(it)
      }
      result?.onSuccess {
        onSuccess()
      }
    }
  }
}