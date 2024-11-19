package com.sd.android.kreedz.feature.account.screen.recover

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.core.utils.AppUtils
import com.sd.lib.compose.input.FTextField
import com.sd.lib.compose.input.FTextFieldIconClear

@Composable
internal fun RecoverInputView(
   modifier: Modifier = Modifier,
   emailState: TextFieldState,
   onClickRecover: () -> Unit,
) {
   val email = emailState.text.toString()
   val isValidEmail = remember(email) { AppUtils.isValidEmail(email) }

   ConstraintLayout(modifier = modifier.fillMaxWidth()) {
      val (refEmail, refRecover) = createRefs()

      InputEmailView(
         emailState = emailState,
         onDone = onClickRecover,
         modifier = Modifier.constrainAs(refEmail) {
            width = Dimension.matchParent
            top.linkTo(parent.top)
         }
      )

      Button(
         onClick = onClickRecover,
         enabled = isValidEmail,
         modifier = Modifier
            .constrainAs(refRecover) {
               centerHorizontallyTo(parent)
               top.linkTo(refEmail.bottom, 36.dp)
            }
            .widthIn(150.dp)
      ) {
         Text(text = "Recover")
      }
   }
}

@Composable
private fun InputEmailView(
   modifier: Modifier = Modifier,
   emailState: TextFieldState,
   onDone: () -> Unit,
) {
   val focusRequester = remember { FocusRequester() }
   LaunchedEffect(focusRequester) {
      focusRequester.requestFocus()
   }

   FTextField(
      state = emailState,
      textStyle = TextStyle(
         fontSize = 16.sp,
         lineHeight = (1.5).em,
      ),
      placeholder = {
         Text(text = "Enter your email...")
      },
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = ImeAction.Done,
      ),
      onKeyboardAction = {
         onDone()
      },
      trailingIcon = {
         FTextFieldIconClear(modifier = Modifier.padding(end = 8.dp))
      },
      modifier = modifier.focusRequester(focusRequester)
   )
}

@Preview
@Composable
private fun Preview() {
   AppTheme {
      RecoverInputView(
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
         emailState = TextFieldState(""),
         onClickRecover = {},
      )
   }
}