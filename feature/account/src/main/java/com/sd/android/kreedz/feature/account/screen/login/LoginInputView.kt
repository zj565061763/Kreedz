package com.sd.android.kreedz.feature.account.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.placeCursorAtEnd
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
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.lib.compose.input.FTextField
import com.sd.lib.compose.input.FTextFieldIconClear

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
   val focusRequesterUsername = remember { FocusRequester() }
   val focusRequesterPassword = remember { FocusRequester() }

   LaunchedEffect(focusRequesterUsername) {
      focusRequesterUsername.requestFocus()
   }

   ConstraintLayout(modifier = modifier.fillMaxWidth()) {
      val (
         refUsername, refPassword,
         refForgot,
         refLogin,
      ) = createRefs()

      InputUsernameView(
         state = usernameState,
         modifier = Modifier
            .constrainAs(refUsername) {
               width = Dimension.matchParent
               top.linkTo(parent.top)
            }
            .focusRequester(focusRequesterUsername)
            .focusProperties { next = focusRequesterPassword }
      )

      InputPasswordView(
         state = passwordState,
         onDone = onClickLogin,
         modifier = Modifier
            .constrainAs(refPassword) {
               width = Dimension.matchParent
               top.linkTo(refUsername.bottom, 16.dp)
            }
            .focusRequester(focusRequesterPassword)
      )

      ForgotView(
         onClickForgotPassword = onClickForgotPassword,
         onClickForgotUsername = onClickForgotUsername,
         onClickRegister = onClickRegister,
         modifier = Modifier.constrainAs(refForgot) {
            top.linkTo(refPassword.bottom, 12.dp)
            end.linkTo(parent.end)
         },
      )

      Button(
         onClick = onClickLogin,
         enabled = usernameState.text.isNotBlank() && passwordState.text.isNotEmpty(),
         modifier = Modifier
            .constrainAs(refLogin) {
               centerHorizontallyTo(parent)
               top.linkTo(refForgot.bottom, 16.dp)
            }
            .widthIn(160.dp)
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
      placeholder = {
         Text(text = "Enter your username...")
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
   onDone: () -> Unit,
) {
   var passwordVisible by remember { mutableStateOf(false) }

   FTextField(
      modifier = modifier.heightIn(56.dp),
      state = state,
      textStyle = TextStyle(
         fontSize = 16.sp,
         lineHeight = (1.5).em,
      ),
      keyboardOptions = KeyboardOptions.Default.copy(
         keyboardType = KeyboardType.Password,
         imeAction = ImeAction.Done,
      ),
      onKeyboardAction = {
         onDone()
      },
      outputTransformation = if (passwordVisible) null else OutputTransformation {
         replace(0, length, "*".repeat(length))
         placeCursorAtEnd()
      },
      placeholder = {
         Text(text = "Enter your password...")
      },
      trailingIcon = {
         if (state.text.isNotBlank()) {
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

//      TextButton(
//         onClick = onClickRegister,
//         contentPadding = PaddingValues(
//            vertical = 0.dp,
//            horizontal = 12.dp,
//         ),
//         modifier = Modifier
//            .defaultMinSize(0.dp, 0.dp)
//            .height(28.dp),
//      ) {
//         Text(
//            text = "Register",
//            color = AppTextColor.small,
//         )
//      }
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