package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sd.lib.compose.utils.fEnabled

@Composable
fun ComBlurCard(
   modifier: Modifier = Modifier,
   onClick: (() -> Unit)? = null,
   padding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 1.dp),
   content: @Composable RowScope.() -> Unit,
) {
   Card(
      modifier = modifier,
      shape = MaterialTheme.shapes.extraSmall,
      colors = CardDefaults.cardColors(
         containerColor = Color.Black.copy(0.3f),
         contentColor = Color.White,
      )
   ) {
      Row(
         modifier = Modifier
            .fEnabled(onClick != null) {
               clickable { onClick?.invoke() }
            }
            .padding(padding),
         verticalAlignment = Alignment.CenterVertically,
      ) {
         content()
      }
   }
}

@Preview
@Composable
private fun PreviewCard() {
   ComBlurCard {
      Text(text = "2024-10-10")
   }
}