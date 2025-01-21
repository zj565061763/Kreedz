package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.constraintlayout.goneIf
import com.sd.lib.compose.layer.Layer
import com.sd.lib.kmp.compose_input.FTextField

@Composable
fun ComInputLayer(
  attach: Boolean,
  onDetachRequest: () -> Unit,
  topLabel: String = "",
  inputState: TextFieldState,
  maxInput: Int?,
  onClickSend: () -> Unit,
) {
  Layer(
    attach = attach,
    onDetachRequest = { onDetachRequest() },
    alignment = Alignment.BottomCenter,
    detachOnTouchBackground = true,
  ) {
    ContentView(
      topLabel = topLabel,
      inputState = inputState,
      maxInput = maxInput,
      onClickSend = onClickSend,
      modifier = Modifier.imePadding(),
    )
  }
}

@Composable
private fun ContentView(
  modifier: Modifier = Modifier,
  topLabel: String,
  inputState: TextFieldState,
  maxInput: Int?,
  onClickSend: () -> Unit,
) {
  ConstraintLayout(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface)
      .padding(top = 16.dp)
  ) {
    val (refTopLabel, refInput, refMaxInput, refSend) = createRefs()

    Text(
      text = topLabel,
      fontSize = 12.sp,
      color = AppTextColor.small,
      modifier = Modifier.constrainAs(refTopLabel) {
        top.linkTo(parent.top)
        centerHorizontallyTo(parent)
        goneIf(topLabel.isBlank())
      }
    )

    InputView(
      inputState = inputState,
      modifier = Modifier.constrainAs(refInput) {
        top.linkTo(refTopLabel.bottom, 16.dp)
        linkTo(
          start = parent.start, end = parent.end,
          startMargin = 16.dp, endMargin = 16.dp,
        )
        width = Dimension.fillToConstraints
      }
    )

    Text(
      text = "${inputState.text.length}/${maxInput}",
      fontSize = 12.sp,
      color = AppTextColor.medium,
      modifier = Modifier.constrainAs(refMaxInput) {
        top.linkTo(refInput.bottom, 4.dp)
        start.linkTo(refInput.start, 2.dp)
        goneIf(maxInput == null)
      }
    )

    SendButton(
      enabled = inputState.text.isNotBlank(),
      onClick = onClickSend,
      modifier = Modifier.constrainAs(refSend) {
        end.linkTo(parent.end, 8.dp)
        top.linkTo(refInput.bottom)
      }
    )
  }
}

@Composable
private fun InputView(
  modifier: Modifier = Modifier,
  inputState: TextFieldState,
) {
  val focusRequester = remember { FocusRequester() }
  LaunchedEffect(focusRequester) {
    focusRequester.requestFocus()
  }

  FTextField(
    modifier = modifier
      .heightIn(64.dp)
      .focusRequester(focusRequester),
    state = inputState,
    textStyle = TextStyle(
      fontSize = 16.sp,
      lineHeight = (1.5).em,
    ),
    maxLines = Int.MAX_VALUE,
  )
}

@Composable
private fun SendButton(
  modifier: Modifier = Modifier,
  enabled: Boolean,
  onClick: () -> Unit,
) {
  IconButton(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled,
    colors = IconButtonDefaults.iconButtonColors(
      contentColor = MaterialTheme.colorScheme.primary,
    )
  ) {
    Icon(
      imageVector = Icons.AutoMirrored.Filled.Send,
      contentDescription = "Send",
    )
  }
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    ContentView(
      topLabel = "",
      inputState = TextFieldState("contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent"),
      maxInput = 200,
      onClickSend = {},
    )
  }
}

@Preview
@Composable
private fun PreviewEmptyInput() {
  AppTheme {
    ContentView(
      topLabel = "",
      inputState = TextFieldState(""),
      maxInput = null,
      onClickSend = {},
    )
  }
}

@Preview
@Composable
private fun PreviewTopLabel() {
  AppTheme {
    ContentView(
      topLabel = "Replay XXX's comment",
      inputState = TextFieldState(""),
      maxInput = null,
      onClickSend = {},
    )
  }
}