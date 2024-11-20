package com.sd.android.kreedz.feature.account.screen.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
internal fun RegisterInputView(
   modifier: Modifier = Modifier,
   emailState: TextFieldState,
   nicknameState: TextFieldState,
   usernameState: TextFieldState,
   passwordState: TextFieldState,
   confirmPasswordState: TextFieldState,
   onClickRegister: () -> Unit,
) {
   val emailFocus = remember { FocusRequester() }
   LaunchedEffect(emailFocus) {
      emailFocus.requestFocus()
   }

   Column(
      modifier = modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(24.dp),
   ) {
      InputEmailView(
         state = emailState,
         modifier = Modifier.focusRequester(emailFocus),
      )

      InputNicknameView(
         state = nicknameState,
      )

      InputUsernameView(
         state = usernameState,
      )

      InputPasswordView(
         state = passwordState,
      )

      InputConfirmPasswordView(
         state = confirmPasswordState,
         onKeyboardDone = onClickRegister,
      )

      Button(
         onClick = onClickRegister,
         enabled = emailState.text.isNotBlank()
            && nicknameState.text.isNotBlank()
            && usernameState.text.isNotBlank()
            && passwordState.text.isNotEmpty()
            && confirmPasswordState.text.isNotEmpty(),
         modifier = Modifier
            .widthIn(160.dp)
            .imePadding(),
      ) {
         Text(text = "Register")
      }
   }
}

@Composable
private fun InputEmailView(
   modifier: Modifier = Modifier,
   state: TextFieldState,
) {
   FTextField(
      modifier = modifier.fillMaxWidth(),
      state = state,
      textStyle = TextStyle(
         fontSize = 16.sp,
         lineHeight = (1.5).em,
      ),
      label = {
         Text(text = "Email")
      },
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = ImeAction.Next,
      ),
      trailingIcon = {
         FTextFieldIconClear(modifier = Modifier.padding(end = 8.dp))
      },
   )
}

@Composable
private fun InputNicknameView(
   modifier: Modifier = Modifier,
   state: TextFieldState,
) {
   FTextField(
      modifier = modifier.fillMaxWidth(),
      state = state,
      textStyle = TextStyle(
         fontSize = 16.sp,
         lineHeight = (1.5).em,
      ),
      label = {
         Text(text = "Nickname")
      },
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = ImeAction.Next,
      ),
      trailingIcon = {
         FTextFieldIconClear(modifier = Modifier.padding(end = 8.dp))
      },
   )
}

@Composable
private fun InputUsernameView(
   modifier: Modifier = Modifier,
   state: TextFieldState,
) {
   FTextField(
      modifier = modifier.fillMaxWidth(),
      state = state,
      textStyle = TextStyle(
         fontSize = 16.sp,
         lineHeight = (1.5).em,
      ),
      label = {
         Text(text = "Username")
      },
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = ImeAction.Next,
      ),
      trailingIcon = {
         FTextFieldIconClear(modifier = Modifier.padding(end = 8.dp))
      },
   )
}

@Composable
private fun InputPasswordView(
   modifier: Modifier = Modifier,
   state: TextFieldState,
) {
   var passwordVisible by remember { mutableStateOf(false) }

   FSecureTextField(
      modifier = modifier.fillMaxWidth(),
      state = state,
      textObfuscationMode = if (passwordVisible) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
      textStyle = TextStyle(
         fontSize = 16.sp,
         lineHeight = (1.5).em,
      ),
      keyboardOptions = KeyboardOptions.fSecure().copy(
         imeAction = ImeAction.Next,
      ),
      label = {
         Text(text = "Password")
      },
      trailingIcon = {
         if (state.text.isNotEmpty()) {
            TextButton(
               onClick = { passwordVisible = !passwordVisible },
            ) {
               Text(
                  text = if (passwordVisible) "hide" else "show",
                  color = AppTextColor.small,
               )
            }
         }
      },
   )
}

@Composable
private fun InputConfirmPasswordView(
   modifier: Modifier = Modifier,
   state: TextFieldState,
   onKeyboardDone: () -> Unit,
) {
   FSecureTextField(
      modifier = modifier.fillMaxWidth(),
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
         Text(text = "Confirm password")
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
      RegisterInputView(
         modifier = Modifier.padding(16.dp),
         emailState = rememberTextFieldState(),
         nicknameState = rememberTextFieldState(),
         usernameState = rememberTextFieldState(),
         passwordState = rememberTextFieldState(),
         confirmPasswordState = rememberTextFieldState(),
         onClickRegister = {},
      )
   }
}