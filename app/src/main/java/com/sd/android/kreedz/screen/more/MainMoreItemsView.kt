package com.sd.android.kreedz.screen.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme

@Composable
fun MainMoreItemsView(
  modifier: Modifier = Modifier,
  onClickFavoriteMaps: () -> Unit,
) {
  Card(
    shape = MaterialTheme.shapes.extraSmall,
    modifier = modifier.fillMaxWidth(),
  ) {
    ItemView(
      title = "Favorite Maps",
      onClick = onClickFavoriteMaps,
    )
  }
}

@Composable
private fun ItemView(
  modifier: Modifier = Modifier,
  title: String,
  onClick: () -> Unit,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .heightIn(56.dp)
      .clickable { onClick() }
      .padding(start = 12.dp, end = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = title,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
    )
    Spacer(Modifier.weight(1f))
    Icon(
      imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
      contentDescription = title,
    )
  }
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    MainMoreItemsView(
      onClickFavoriteMaps = {},
    )
  }
}