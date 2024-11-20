package com.sd.android.kreedz.feature.account.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.input.FSecureTextField
import com.sd.lib.compose.input.FTextField
import com.sd.lib.compose.input.FTextFieldIconClear
import com.sd.lib.compose.input.fSecure

@Composable
internal fun LoginInputView(
   modifier: Modifier = Modifier,
   usernameState: TextFieldState,
   passwordState: TextFieldState,
   onClickLogin: () -> Unit,
   onClickForgotPassword: () -> Unit,
   onClickForgotUsername: () -> Unit,
   onClickRegister: () -> Unit,
) {
   val usernameFocus = remember { FocusRequester() }
   LaunchedEffect(usernameFocus) {
      usernameFocus.requestFocus()
   }

   Column(
      modifier = modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
   ) {
      InputUsernameView(
         state = usernameState,
         modifier = Modifier
            .fillMaxWidth()
            .focusRequester(usernameFocus)
      )

      Spacer(Modifier.height(24.dp))
      InputPasswordView(
         state = passwordState,
         onKeyboardDone = onClickLogin,
         modifier = Modifier.fillMaxWidth(),
      )

      Spacer(Modifier.height(16.dp))
      ForgotView(
         onClickForgotPassword = onClickForgotPassword,
         onClickForgotUsername = onClickForgotUsername,
         onClickRegister = onClickRegister,
         modifier = Modifier.align(Alignment.End),
      )

      Spacer(Modifier.height(16.dp))
      Button(
         onClick = onClickLogin,
         enabled = usernameState.text.isNotBlank() && passwordState.text.isNotEmpty(),
         modifier = Modifier.widthIn(160.dp),
      ) {
         Text(text = "Login")
      }
   }
}

@Composable
private fun InputUsernameView(
   modifier: Modifier = Modifier,
   state: TextFieldState,
) {
   FTextField(
      modifier = modifier,
      state = state,
      textStyle = TextStyle(
         fontSize = 16.sp,
         lineHeight = (1.5).em,
      ),
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = ImeAction.Next,
      ),
      label = {
         Text(text = "Username")
      },
      trailingIcon = {
         FTextFieldIconClear(modifier = Modifier.padding(end = 8.dp))
      },
   )
}

@Composable
private fun InputPasswordView(
   modifier: Modifier = Modifier,
   state: TextFieldState,
   onKeyboardDone: () -> Unit,
) {
   FSecureTextField(
      modifier = modifier,
      state = state,
      textStyle = TextStyle(
         fontSize = 16.sp,
         lineHeight = (1.5).em,
      ),
      keyboardOptions = KeyboardOptions.fSecure().copy(
         imeAction = ImeAction.Done,
      ),
      onKeyboardAction = {
         onKeyboardDone()
      },
      label = {
         Text(text = "Password")
      },
      trailingIcon = {
         FTextFieldIconClear(modifier = Modifier.padding(end = 8.dp))
      },
   )
}

@Composable
private fun ForgotView(
   modifier: Modifier = Modifier,
   onClickForgotPassword: () -> Unit,
   onClickForgotUsername: () -> Unit,
   onClickRegister: () -> Unit,
) {
   Column(
      modifier = modifier,
      horizontalAlignment = Alignment.End,
   ) {
      TextButton(
         onClick = onClickForgotPassword,
         contentPadding = PaddingValues(
            vertical = 0.dp,
            horizontal = 12.dp,
         ),
         modifier = Modifier
            .defaultMinSize(0.dp, 0.dp)
            .height(28.dp),
      ) {
         Text(
            text = "Forgot password?",
            color = AppTextColor.small,
         )
      }

      TextButton(
         onClick = onClickForgotUsername,
         contentPadding = PaddingValues(
            vertical = 0.dp,
            horizontal = 12.dp,
         ),
         modifier = Modifier
            .defaultMinSize(0.dp, 0.dp)
            .height(28.dp),
      ) {
         Text(
            text = "Forgot username?",
            color = AppTextColor.small,
         )
      }

      TextButton(
         onClick = onClickRegister,
         contentPadding = PaddingValues(
            vertical = 0.dp,
            horizontal = 12.dp,
         ),
         modifier = Modifier
            .defaultMinSize(0.dp, 0.dp)
            .height(28.dp),
      ) {
         Text(
            text = "Register",
            color = AppTextColor.small,
         )
      }
   }
}

@Preview
@Composable
private fun Preview() {
   AppTheme {
      LoginInputView(
         modifier = Modifier.padding(16.dp),
         usernameState = rememberTextFieldState(),
         passwordState = rememberTextFieldState(),
         onClickLogin = {},
         onClickForgotPassword = {},
         onClickForgotUsername = {},
         onClickRegister = {},
      )
   }
}