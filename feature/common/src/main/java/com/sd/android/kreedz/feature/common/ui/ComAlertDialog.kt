package com.sd.android.kreedz.feature.common.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.sd.android.kreedz.core.ui.AppTheme

@Composable
fun ComAlertDialog(
   onDismissRequest: () -> Unit,
   text: @Composable (() -> Unit),
   confirmButton: @Composable (() -> Unit) = {
      TextButton(onClick = onDismissRequest) {
         Text(text = "OK")
      }
   },
   dismissButton: @Composable (() -> Unit)? = null,
   title: @Composable (() -> Unit)? = null,
) {
   AlertDialog(
      shape = MaterialTheme.shapes.medium,
      properties = DialogProperties(
         dismissOnClickOutside = false,
      ),
      onDismissRequest = onDismissRequest,
      text = text,
      confirmButton = confirmButton ?: {},
      dismissButton = dismissButton,
      title = title,
   )
}

@Preview
@Composable
private fun Preview() {
   AppTheme {
      ComAlertDialog(
         onDismissRequest = {},
         text = {
            Text("text")
         },
         confirmButton = {
            TextButton(onClick = {}) {
               Text(text = "Confirm")
            }
         },
         dismissButton = {
            TextButton(onClick = {}) {
               Text(text = "Dismiss")
            }
         },
         title = {
            Text("title")
         }
      )
   }
}