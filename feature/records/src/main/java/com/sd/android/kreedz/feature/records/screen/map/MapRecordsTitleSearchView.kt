package com.sd.android.kreedz.feature.records.screen.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.input.FTextField
import com.sd.lib.compose.input.FTextFieldIconClear
import com.sd.lib.compose.input.FTextFieldIconContainer
import com.sd.lib.compose.input.FTextFieldIndicatorOutline

@Composable
internal fun MapRecordsTitleSearchView(
  modifier: Modifier = Modifier,
  textFieldState: TextFieldState,
) {
  FTextField(
    modifier = modifier.defaultMinSize(1.dp, 36.dp),
    state = textFieldState,
    contentPadding = PaddingValues(0.dp),
    textStyle = TextStyle(
      fontSize = 14.sp,
      lineHeight = (1.2).em,
    ),
    placeholder = {
      Text(text = "Search...")
    },
    leadingIcon = {
      FTextFieldIconContainer(Modifier.padding(start = 8.dp)) {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Search",
          modifier = Modifier.size(18.dp),
        )
      }
    },
    trailingIcon = {
      FTextFieldIconClear(Modifier.padding(end = 8.dp))
    },
    indicator = {
      FTextFieldIndicatorOutline(shape = CircleShape)
    },
  )
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    MapRecordsTitleSearchView(
      modifier = Modifier
        .width(200.dp)
        .padding(16.dp),
      textFieldState = TextFieldState("kz"),
    )
  }
}

@Preview
@Composable
private fun PreviewEmpty() {
  AppTheme {
    MapRecordsTitleSearchView(
      modifier = Modifier
        .width(200.dp)
        .padding(16.dp),
      textFieldState = TextFieldState(""),
    )
  }
}