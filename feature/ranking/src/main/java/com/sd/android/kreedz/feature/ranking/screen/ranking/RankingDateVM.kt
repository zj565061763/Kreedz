package com.sd.android.kreedz.feature.ranking.screen.ranking

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.lib.date.FDate
import com.sd.lib.date.fCurrentDate

internal class RankingDateVM : BaseViewModel<RankingDateVM.State, Unit>(State()) {

  fun showSelectDate() {
    updateState {
      it.copy(showSelectDate = true)
    }
  }

  fun selectDate(date: FDate?) {
    if (!state.showSelectDate) return

    if (date == null) {
      updateState { it.copy(showSelectDate = false) }
      return
    }

    val overflow = date >= fCurrentDate()
    updateState {
      it.copy(
        showSelectDate = false,
        selectedDate = if (overflow) null else date,
        selectedDateStr = if (overflow) "" else date.toString(),
      )
    }
  }

  @Immutable
  data class State(
    val showSelectDate: Boolean = false,
    val selectedDate: FDate? = null,
    val selectedDateStr: String = "",
  )
}