package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme

@Composable
fun ComErrorView(
  modifier: Modifier = Modifier,
  error: Throwable,
  title: String = "Error",
  onClickRetry: (() -> Unit)? = null,
) {
  ComErrorView(
    error = error.toString(),
    modifier = modifier,
    title = title,
    onClickRetry = onClickRetry,
  )
}

@Composable
fun ComErrorView(
  modifier: Modifier = Modifier,
  error: String,
  title: String = "Error",
  onClickRetry: (() -> Unit)? = null,
) {
  Column(
    modifier = modifier.padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Text(
      text = title,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
    )

    Text(
      text = error,
      fontSize = 14.sp,
      color = AppTextColor.medium,
    )

    if (onClickRetry != null) {
      Button(onClick = onClickRetry) {
        Text(text = "Retry")
      }
    }
  }
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    ComErrorView(
      error = "Load error",
      title = "Error",
      onClickRetry = null,
    )
  }
}

@Preview
@Composable
private fun PreviewRetry() {
  AppTheme {
    ComErrorView(
      error = "Load error",
      title = "Error",
      onClickRetry = {},
    )
  }
}