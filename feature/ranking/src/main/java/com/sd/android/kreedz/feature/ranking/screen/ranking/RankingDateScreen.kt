package com.sd.android.kreedz.feature.ranking.screen.ranking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sd.android.kreedz.feature.common.ui.ComDatePickerLayer

@Composable
internal fun RankingDateScreen(
  vm: RankingDateVM,
) {
  val state by vm.stateFlow.collectAsStateWithLifecycle()
  ComDatePickerLayer(
    attach = state.showSelectDate,
    date = state.selectedDate,
    onDone = { vm.selectDate(it) },
  )
}