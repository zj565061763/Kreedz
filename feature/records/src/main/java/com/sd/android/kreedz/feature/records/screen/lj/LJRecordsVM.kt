package com.sd.android.kreedz.feature.records.screen.lj

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.GroupedLJRecordsModel
import com.sd.android.kreedz.data.repository.LJRecordsRepository
import com.sd.lib.kmp.coroutines.FLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

internal class LJRecordsVM : BaseViewModel<LJRecordsVM.State, Any>(State()) {
  private val _repository = LJRecordsRepository()
  private val _loader = FLoader()

  fun load() {
    vmLaunch {
      _loader.load {
        loadData()
      }
    }
  }

  fun retry() {
    vmLaunch {
      _loader.load {
        delay(500)
        loadData()
      }
    }
  }

  private suspend fun loadData() {
    val data = _repository.getLJRecords()
    val groups = withContext(Dispatchers.IO) { data.map { it.type } }
    updateState {
      it.copy(
        groups = groups,
        records = data,
      )
    }
  }

  init {
    vmLaunch {
      _loader.stateFlow.collect { data ->
        updateState {
          it.copy(
            isLoading = data.isLoading,
            result = data.result,
          )
        }
      }
    }
  }

  @Immutable
  data class State(
    val isLoading: Boolean = false,
    val result: Result<Unit>? = null,

    val groups: List<String> = emptyList(),
    val records: List<GroupedLJRecordsModel> = emptyList(),
  )
}