package com.sd.android.kreedz.feature.news.screen.release

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.LatestRecordGroupModel
import com.sd.android.kreedz.data.repository.LatestReleaseRepository
import com.sd.lib.kmp.coroutines.FLoader

class LatestReleaseVM : BaseViewModel<LatestReleaseVM.State, Any>(State()) {
  private val _repository = LatestReleaseRepository()
  private val _loader = FLoader()

  fun init() {
    if (state.records.isEmpty()) {
      refresh()
    }
  }

  fun refresh() {
    vmLaunch {
      _loader.load {
        _repository.sync()
      }.onFailure { error ->
        sendEffect(error)
      }
    }
  }

  init {
    vmLaunch {
      _loader.loadingFlow.collect { data ->
        updateState {
          it.copy(isLoading = data)
        }
      }
    }

    vmLaunch {
      _repository.getLatestReleaseFlow().collect { data ->
        updateState {
          it.copy(
            newsId = data.newsId,
            newsName = data.newsName,
            records = data.records,
          )
        }
      }
    }
  }

  @Immutable
  data class State(
    val isLoading: Boolean = false,
    val newsId: String? = null,
    val newsName: String? = null,
    val records: List<LatestRecordGroupModel> = emptyList(),
  )
}