package com.sd.android.kreedz.feature.chat.screen.chatbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.layer.Layer

@Composable
internal fun ChatBoxMessageMenuLayer(
   attach: Boolean,
   onDetachRequest: () -> Unit,
   onClickReply: (() -> Unit)?,
   onClickCopy: (() -> Unit)?,
   onClickDelete: (() -> Unit)?,
) {
   Layer(
      attach = attach,
      onDetachRequest = { onDetachRequest() },
      alignment = Alignment.BottomCenter,
      detachOnTouchBackground = true,
   ) {
      ContentView(
         onClickReply = onClickReply,
         onClickCopy = onClickCopy,
         onClickDelete = onClickDelete,
         modifier = Modifier.navigationBarsPadding()
      )
   }
}

@Composable
private fun ContentView(
   modifier: Modifier = Modifier,
   onClickReply: (() -> Unit)?,
   onClickCopy: (() -> Unit)?,
   onClickDelete: (() -> Unit)?,
) {
   Column(
      modifier = modifier
         .fillMaxWidth()
         .background(MaterialTheme.colorScheme.surface),
   ) {
      if (onClickReply != null) {
         ItemView(
            text = "Reply",
            onClick = onClickReply,
         )
      }
      if (onClickCopy != null) {
         ItemView(
            text = "Copy",
            onClick = onClickCopy,
         )
      }
      if (onClickDelete != null) {
         ItemView(
            text = "Delete",
            onClick = onClickDelete,
         )
      }
   }
}

@Composable
private fun ItemView(
   modifier: Modifier = Modifier,
   text: String,
   onClick: () -> Unit,
) {
   Box(
      modifier = modifier
         .fillMaxWidth()
         .heightIn(58.dp)
         .clickable { onClick() },
      contentAlignment = Alignment.Center,
   ) {
      Text(
         text = text,
         fontSize = 16.sp,
      )
   }
}

@Preview
@Composable
private fun Preview() {
   AppTheme {
      ContentView(
         onClickReply = {},
         onClickCopy = {},
         onClickDelete = {},
      )
   }
}