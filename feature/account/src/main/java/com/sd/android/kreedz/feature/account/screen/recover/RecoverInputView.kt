package com.sd.android.kreedz.feature.account.screen.recover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.input.FTextField
import com.sd.lib.compose.input.FTextFieldIconClear

@Composable
internal fun RecoverInputView(
   modifier: Modifier = Modifier,
   emailState: TextFieldState,
   onClickRecover: () -> Unit,
) {
   val emailFocus = remember { FocusRequester() }
   LaunchedEffect(emailFocus) {
      emailFocus.requestFocus()
   }

   Column(
      modifier = modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
   ) {
      InputEmailView(
         emailState = emailState,
         onKeyboardDone = onClickRecover,
         modifier = Modifier.focusRequester(emailFocus),
      )

      Spacer(Modifier.height(36.dp))
      Button(
         onClick = onClickRecover,
         enabled = emailState.text.isNotBlank(),
         modifier = Modifier.widthIn(150.dp),
      ) {
         Text(text = "Recover")
      }
   }
}

@Composable
private fun InputEmailView(
   modifier: Modifier = Modifier,
   emailState: TextFieldState,
   onKeyboardDone: () -> Unit,
) {
   FTextField(
      modifier = modifier.fillMaxWidth(),
      state = emailState,
      textStyle = TextStyle(
         fontSize = 16.sp,
         lineHeight = (1.5).em,
      ),
      keyboardOptions = KeyboardOptions.Default.copy(
         keyboardType = KeyboardType.Email,
         imeAction = ImeAction.Done,
      ),
      onKeyboardAction = {
         onKeyboardDone()
      },
      label = {
         Text(text = "Email")
      },
      trailingIcon = {
         FTextFieldIconClear(modifier = Modifier.padding(end = 8.dp))
      },
   )
}

@Preview
@Composable
private fun Preview() {
   AppTheme {
      RecoverInputView(
         modifier = Modifier.padding(16.dp),
         emailState = TextFieldState("666666666@qq.com"),
         onClickRecover = {},
      )
   }
}

@Preview
@Composable
private fun PreviewEmpty() {
   AppTheme {
      RecoverInputView(
         modifier = Modifier.padding(16.dp),
         emailState = TextFieldState(""),
         onClickRecover = {},
      )
   }
}