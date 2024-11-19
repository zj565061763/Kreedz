package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme

@Composable
fun ComCountryTextViewExtraSmall(
   modifier: Modifier = Modifier,
   country: String?,
   text: CharSequence?,
   fontColor: Color = LocalContentColor.current,
) {
   CountryTextView(
      modifier = modifier,
      country = country,
      text = text,
      imageWidth = 12.dp,
      fontSize = 11.sp,
      textColor = fontColor,
      spacing = 2.dp,
   )
}

@Composable
fun ComCountryTextViewSmall(
   modifier: Modifier = Modifier,
   country: String?,
   text: CharSequence?,
   textColor: Color = LocalContentColor.current,
) {
   CountryTextView(
      modifier = modifier,
      country = country,
      text = text,
      imageWidth = 14.dp,
      fontSize = 12.sp,
      textColor = textColor,
      spacing = 2.dp,
   )
}

@Composable
fun ComCountryTextViewMedium(
   modifier: Modifier = Modifier,
   country: String?,
   text: CharSequence?,
   textColor: Color = LocalContentColor.current,
) {
   CountryTextView(
      modifier = modifier,
      country = country,
      text = text,
      imageWidth = 16.dp,
      fontSize = 14.sp,
      textColor = textColor,
      spacing = 3.dp,
   )
}

@Composable
fun ComCountryTextViewLarge(
   modifier: Modifier = Modifier,
   country: String?,
   text: CharSequence?,
   textColor: Color = LocalContentColor.current,
) {
   CountryTextView(
      modifier = modifier,
      country = country,
      text = text,
      imageWidth = 20.dp,
      fontSize = 16.sp,
      textColor = textColor,
      spacing = 3.dp,
   )
}

@Composable
private fun CountryTextView(
   modifier: Modifier = Modifier,
   country: String?,
   text: CharSequence?,
   imageWidth: Dp,
   fontSize: TextUnit,
   textColor: Color,
   spacing: Dp,
) {
   ComCountryContentView(
      modifier = modifier,
      country = country,
      imageWidth = imageWidth,
   ) {
      Spacer(modifier = Modifier.width(spacing))
      CountryText(
         text = text.takeUnless { it.isNullOrBlank() } ?: "N/A",
         color = textColor,
         fontSize = fontSize,
         fontWeight = FontWeight.Medium,
      )
   }
}

@Composable
private fun CountryText(
   modifier: Modifier = Modifier,
   text: CharSequence,
   color: Color,
   fontSize: TextUnit,
   fontWeight: FontWeight?,
) {
   if (text is AnnotatedString) {
      Text(
         modifier = modifier,
         text = text,
         color = color,
         fontSize = fontSize,
         fontWeight = fontWeight,
      )
   } else {
      val string = text.toString().takeIf { it.isNotBlank() } ?: "N/A"
      Text(
         modifier = modifier,
         text = string,
         color = color,
         fontSize = fontSize,
         fontWeight = fontWeight,
      )
   }
}

@Preview
@Composable
private fun PreviewEmpty() {
   AppTheme {
      ComCountryTextViewExtraSmall(
         country = "",
         text = "",
      )
   }
}

@Preview
@Composable
private fun PreviewExtraSmall() {
   AppTheme {
      ComCountryTextViewExtraSmall(
         country = "cn",
         text = "zhengjun",
      )
   }
}

@Preview
@Composable
private fun PreviewSmall() {
   AppTheme {
      ComCountryTextViewSmall(
         country = "cn",
         text = "zhengjun",
      )
   }
}

@Preview
@Composable
private fun PreviewMedium() {
   AppTheme {
      ComCountryTextViewMedium(
         country = "cn",
         text = "zhengjun",
      )
   }
}

@Preview
@Composable
private fun PreviewLarge() {
   AppTheme {
      ComCountryTextViewLarge(
         country = "cn",
         text = "zhengjun",
      )
   }
}