package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sd.android.kreedz.feature.common.R

@Composable
fun ComCountryContentView(
   country: String?,
   modifier: Modifier = Modifier,
   imageWidth: Dp = 20.dp,
   content: @Composable RowScope.() -> Unit,
) {
   Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
   ) {
      ComCountryImageView(
         country = country,
         modifier = Modifier.width(imageWidth),
      )
      content()
   }
}

@Composable
fun ComCountryImageView(
   country: String?,
   modifier: Modifier = Modifier,
) {
   val imageId = countryImage(country)
   Image(
      modifier = modifier,
      painter = painterResource(imageId),
      contentDescription = "Country $country",
      contentScale = ContentScale.FillWidth,
   )
}

@Composable
private fun countryImage(country: String?): Int {
   if (country.isNullOrBlank()) return R.drawable.country_unknown
   val context = LocalContext.current
   val name = "country_$country".lowercase()
   return remember(context, name) {
      context.resources.getIdentifier(
         name,
         "drawable",
         context.packageName
      )
   }.takeUnless { it == 0 } ?: R.drawable.country_unknown
}

@Preview
@Composable
private fun PreviewView() {
   ComCountryContentView("cn") {
      Text(text = "zhengjun")
   }
}