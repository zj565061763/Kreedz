package com.sd.android.kreedz.screen.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.input.FTextField
import com.sd.lib.compose.input.FTextFieldIconClear
import com.sd.lib.compose.input.FTextFieldIconContainer
import com.sd.lib.compose.input.FTextFieldIndicatorOutline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTitleView(
  modifier: Modifier = Modifier,
  textFieldState: TextFieldState,
  isReadyToSearch: Boolean,
  isSearching: Boolean,
  onClickBack: () -> Unit,
  onClickSearch: () -> Unit,
) {
  TopAppBar(
    modifier = modifier.fillMaxWidth(),
    title = {
      SearchBar(
        textFieldState = textFieldState,
        isReadyToSearch = isReadyToSearch,
        isSearching = isSearching,
        onKeyboardAction = onClickSearch,
        modifier = Modifier.fillMaxWidth(),
      )
    },
    navigationIcon = {
      IconButton(onClick = onClickBack) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
        )
      }
    },
  )
}

@Composable
private fun SearchBar(
  modifier: Modifier = Modifier,
  textFieldState: TextFieldState,
  isReadyToSearch: Boolean,
  isSearching: Boolean,
  onKeyboardAction: () -> Unit,
) {
  val focusRequester = remember { FocusRequester() }
  LaunchedEffect(focusRequester) {
    focusRequester.requestFocus()
  }

  FTextField(
    modifier = modifier
      .defaultMinSize(1.dp, 36.dp)
      .focusRequester(focusRequester),
    state = textFieldState,
    contentPadding = PaddingValues(0.dp),
    textStyle = TextStyle(
      fontSize = 14.sp,
      lineHeight = (1.2).em,
    ),
    keyboardOptions = KeyboardOptions.Default.copy(
      imeAction = ImeAction.Search,
    ),
    onKeyboardAction = { onKeyboardAction() },
    placeholder = {
      Text(text = "Search...")
    },
    leadingIcon = {
      FTextFieldIconContainer(Modifier.padding(start = 8.dp)) {
        if (isSearching) {
          CircularProgressIndicator(
            strokeWidth = 1.dp,
            modifier = Modifier.size(14.dp),
          )
        } else {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier.size(18.dp),
          )
        }
      }
    },
    trailingIcon = {
      FTextFieldIconClear(Modifier.padding(end = 8.dp))
    },
    indicator = {
      FTextFieldIndicatorOutline(
        shape = CircleShape,
        color = with(MaterialTheme.colorScheme) {
          if (isReadyToSearch) primary else onSurface.copy(0.4f)
        }
      )
    },
  )
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    SearchTitleView(
      modifier = Modifier.fillMaxWidth(),
      textFieldState = rememberTextFieldState(),
      isReadyToSearch = false,
      isSearching = false,
      onClickBack = {},
      onClickSearch = {},
    )
  }
}