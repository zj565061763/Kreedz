package com.sd.android.kreedz.feature.common.ui

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sd.android.kreedz.core.ui.AppTheme

@Composable
fun ComErrorDialog(
   error: String,
   onDismissRequest: () -> Unit,
   onClickRetry: (() -> Unit)? = null,
) {
   ComAlertDialog(
      onDismissRequest = onDismissRequest,
      confirmButton = {
         if (onClickRetry != null) {
            TextButton(
               onClick = {
                  onDismissRequest()
                  onClickRetry()
               }
            ) {
               Text(text = "Retry")
            }
         }
      },
      dismissButton = {
         TextButton(onClick = onDismissRequest) {
            Text(text = "OK")
         }
      },
      title = {
         Text(text = "Error")
      },
      text = {
         Text(text = error)
      },
   )
}

@Preview
@Composable
private fun Preview() {
   AppTheme {
      ComErrorDialog(
         error = "Load error",
         onDismissRequest = {},
         onClickRetry = {},
      )
   }
}