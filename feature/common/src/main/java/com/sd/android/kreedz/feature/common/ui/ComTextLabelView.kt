package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme

@Composable
fun ComTextLabelView(
   modifier: Modifier = Modifier,
   text: String,
   label: String,
   textFontSize: TextUnit = 14.sp,
   textColor: Color = AppTextColor.primary,
) {
   Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
   ) {
      Text(
         text = text,
         color = textColor,
         fontSize = textFontSize,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.alignByBaseline(),
      )
      Spacer(modifier = Modifier.width(1.dp))
      Text(
         text = label,
         color = AppTextColor.small,
         fontSize = 12.sp,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.alignByBaseline()
      )
   }
}

@Preview
@Composable
private fun PreviewView() {
   AppTheme {
      ComTextLabelView(
         text = "258",
         label = "block"
      )
   }
}