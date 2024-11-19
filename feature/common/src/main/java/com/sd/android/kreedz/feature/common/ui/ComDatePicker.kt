package com.sd.android.kreedz.feature.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sd.lib.compose.layer.Layer
import com.sd.lib.compose.wheel_picker.CurrentIndex
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.FWheelPickerFocusVertical
import com.sd.lib.compose.wheel_picker.FWheelPickerState
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import com.sd.lib.date.FDate
import com.sd.lib.date.FDateSelector
import com.sd.lib.date.fCurrentDate
import com.sd.lib.date.fDate
import com.sd.lib.date.selectDayOfMonthWithIndex
import com.sd.lib.date.selectMonthWithIndex
import com.sd.lib.date.selectYearWithIndex

@Composable
fun ComDatePickerLayer(
   attach: Boolean,
   date: FDate?,
   onDone: (FDate?) -> Unit,
) {
   val selector = remember {
      FDateSelector(
         startDate = fDate(2000, 1, 1),
         endDate = fCurrentDate(),
      )
   }

   LaunchedEffect(selector, date) {
      selector.setDate(date ?: fCurrentDate())
   }

   Layer(
      attach = attach,
      onDetachRequest = { onDone(null) },
      alignment = Alignment.BottomCenter,
      detachOnTouchBackground = true,
   ) {
      Card(shape = MaterialTheme.shapes.extraSmall) {
         Picker(
            selector = selector,
            onDone = onDone,
         )
      }
   }
}

@Composable
private fun Picker(
   modifier: Modifier = Modifier,
   selector: FDateSelector,
   onDone: (FDate?) -> Unit,
) {
   val state by selector.stateFlow.collectAsStateWithLifecycle()

   Column(modifier = modifier.fillMaxWidth()) {
      TextButton(
         modifier = Modifier
            .align(Alignment.End)
            .padding(horizontal = 16.dp, vertical = 12.dp),
         onClick = { onDone(selector.date) },
      ) {
         Text(text = "Done")
      }
      PickerView(
         listYear = state.listYear,
         listMonth = state.listMonth,
         listDayOfMonth = state.listDayOfMonth,
         indexOfYear = state.indexOfYear,
         indexOfMonth = state.indexOfMonth,
         indexOfDayOfMonth = state.indexOfDayOfMonth,
         onYearIndexChange = {
            selector.selectYearWithIndex(it)
         },
         onMonthIndexChange = {
            selector.selectMonthWithIndex(it)
         },
         onDayOfMonthIndexChange = {
            selector.selectDayOfMonthWithIndex(it)
         },
      )
   }
}

@Composable
private fun PickerView(
   modifier: Modifier = Modifier,
   listYear: List<Int>,
   listMonth: List<Int>,
   listDayOfMonth: List<Int>,
   indexOfYear: Int,
   indexOfMonth: Int,
   indexOfDayOfMonth: Int,
   onYearIndexChange: suspend (Int) -> Unit,
   onMonthIndexChange: suspend (Int) -> Unit,
   onDayOfMonthIndexChange: suspend (Int) -> Unit,
) {
   if (indexOfYear < 0) return
   if (indexOfMonth < 0) return
   if (indexOfDayOfMonth < 0) return

   val yearState = rememberFWheelPickerState(indexOfYear)
   val monthState = rememberFWheelPickerState(indexOfMonth)
   val dayOfMonthState = rememberFWheelPickerState(indexOfDayOfMonth)

   yearState.CurrentIndex(onYearIndexChange)
   monthState.CurrentIndex(onMonthIndexChange)
   dayOfMonthState.CurrentIndex(onDayOfMonthIndexChange)

   Row(
      modifier = modifier
         .fillMaxWidth()
         .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
   ) {
      // Year
      WheelPicker(
         modifier = Modifier.weight(1f),
         count = listYear.size,
         state = yearState,
      ) { index ->
         listYear.getOrNull(index)?.also { value ->
            Text(
               text = value.toString(),
               fontSize = 16.sp,
               fontWeight = FontWeight.Medium,
            )
         }
      }

      // Month
      WheelPicker(
         modifier = Modifier.weight(1f),
         count = listMonth.size,
         state = monthState,
      ) { index ->
         listMonth.getOrNull(index)?.also { value ->
            Text(
               text = value.toString(),
               fontSize = 16.sp,
               fontWeight = FontWeight.Medium,
            )
         }
      }

      // Day of month
      WheelPicker(
         modifier = Modifier.weight(1f),
         count = listDayOfMonth.size,
         state = dayOfMonthState,
      ) { index ->
         listDayOfMonth.getOrNull(index)?.also { value ->
            Text(
               text = value.toString(),
               fontSize = 16.sp,
               fontWeight = FontWeight.Medium,
            )
         }
      }
   }
}

@Composable
private fun WheelPicker(
   modifier: Modifier = Modifier,
   count: Int,
   state: FWheelPickerState,
   content: @Composable (index: Int) -> Unit,
) {
   FVerticalWheelPicker(
      modifier = modifier,
      count = count,
      state = state,
      focus = {
         FWheelPickerFocusVertical(
            dividerColor = LocalContentColor.current
         )
      },
   ) { index ->
      content(index)
   }
}

@Preview
@Composable
private fun Preview() {
   PickerView(
      listYear = (2020..2024).toList(),
      listMonth = (1..12).toList(),
      listDayOfMonth = (1..30).toList(),
      indexOfYear = 5,
      indexOfMonth = 5,
      indexOfDayOfMonth = 5,
      onYearIndexChange = {},
      onMonthIndexChange = {},
      onDayOfMonthIndexChange = {},
   )
}