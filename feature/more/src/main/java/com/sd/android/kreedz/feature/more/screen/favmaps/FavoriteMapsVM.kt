package com.sd.android.kreedz.feature.more.screen.favmaps

import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.MapRecordModel
import com.sd.android.kreedz.data.repository.FavoriteMapRepository
import com.sd.lib.coroutines.FLoader
import kotlinx.coroutines.delay

internal class FavoriteMapsVM : BaseViewModel<FavoriteMapsVM.State, Unit>(State()) {
  private val _repository = FavoriteMapRepository()
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
    _repository.sync()
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

    vmLaunch {
      _repository.getFavoriteMapsFlow()
        .collect { data ->
          updateState {
            it.copy(maps = data)
          }
        }
    }
  }

  data class State(
    val isLoading: Boolean = false,
    val result: Result<Unit>? = null,

    val maps: List<MapRecordModel> = emptyList(),
  )
}