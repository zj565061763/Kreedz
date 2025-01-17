package com.sd.android.kreedz.feature.ranking.screen.top

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.TopCountryRankingModel
import com.sd.android.kreedz.data.model.TopPlayerRankingModel
import com.sd.android.kreedz.data.repository.TopRankingRepository
import com.sd.lib.kmp.coroutines.FLoader

class TopRankingVM : BaseViewModel<TopRankingVM.State, Any>(State()) {
  private val _repository = TopRankingRepository()
  private val _loader = FLoader()

  fun init() {
    if (state.topPlayer.isEmpty()
      || state.topCountry.isEmpty()
    ) {
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
      _repository.getTopPlayerRankingFlow().collect { data ->
        updateState {
          it.copy(topPlayer = data)
        }
      }
    }

    vmLaunch {
      _repository.getTopCountryRankingFlow().collect { data ->
        updateState {
          it.copy(topCountry = data)
        }
      }
    }
  }

  @Immutable
  data class State(
    val isLoading: Boolean = false,
    val topPlayer: List<TopPlayerRankingModel> = emptyList(),
    val topCountry: List<TopCountryRankingModel> = emptyList(),
  )
}