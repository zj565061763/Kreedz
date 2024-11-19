package com.sd.android.kreedz.feature.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

class FixedTextFieldValue {
   private lateinit var _value: String
   private lateinit var _onValueChange: (String) -> Unit
   private lateinit var _textFieldValueState: MutableState<TextFieldValue>

   fun onValueChange(textFieldValue: TextFieldValue) {
      _textFieldValueState.value = textFieldValue

      val valueChanged = _value != textFieldValue.text
      _value = textFieldValue.text

      if (valueChanged) {
         _onValueChange(textFieldValue.text)
      }
   }

   @Composable
   fun fix(
      value: String,
      onValueChange: (String) -> Unit,
   ): TextFieldValue {
      _value = value
      _onValueChange = onValueChange

      val textFieldValueState = remember {
         mutableStateOf(TextFieldValue(text = value)).also {
            _textFieldValueState = it
         }
      }

      val stateValue = textFieldValueState.value
      val textFieldValue = if (stateValue.text.isEmpty() && value.isNotEmpty()) {
         stateValue.copy(text = value, selection = TextRange(value.length))
      } else {
         stateValue.copy(text = value)
      }

      SideEffect {
         if (textFieldValue.selection != stateValue.selection ||
            textFieldValue.composition != stateValue.composition
         ) {
            textFieldValueState.value = textFieldValue
         }
      }

      return textFieldValue
   }
}
