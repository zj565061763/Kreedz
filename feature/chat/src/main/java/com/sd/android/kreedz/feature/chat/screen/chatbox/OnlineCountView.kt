package com.sd.android.kreedz.feature.chat.screen.chatbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.feature.common.ui.ComTextLabelView

@Composable
internal fun OnlineCountView(
   modifier: Modifier = Modifier,
   guestsCount: Int,
   usersCount: Int,
) {
   Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
   ) {
      ComTextLabelView(
         text = guestsCount.toString(),
         label = "guests",
      )

      ComTextLabelView(
         text = usersCount.toString(),
         label = "members",
      )
   }
}

@Preview
@Composable
private fun Preview() {
   AppTheme {
      OnlineCountView(
         guestsCount = 4,
         usersCount = 2,
      )
   }
}