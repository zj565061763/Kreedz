package com.sd.android.kreedz.screen.map

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.RecordModel
import com.sd.android.kreedz.data.model.UserModel
import com.sd.android.kreedz.data.repository.FavoriteMapRepository
import com.sd.android.kreedz.data.repository.MapImageRepository
import com.sd.android.kreedz.data.repository.MapRepository
import com.sd.lib.coroutines.FLoader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
class MapVM : BaseViewModel<MapVM.State, Any>(State()) {
  private val _repository = MapRepository()
  private val _imageRepository = MapImageRepository()
  private val _favoriteRepository = FavoriteMapRepository()

  private val _loader = FLoader()

  private val _mapIdFlow = stateFlow
    .map { it.mapId }
    .filter { it.isNotBlank() }
    .distinctUntilChanged()

  fun load(mapId: String) {
    updateState {
      it.copy(mapId = mapId)
    }
  }

  fun retry() {
    vmLaunch {
      syncMap(state.mapId)
    }
  }

  fun clickFavorite() {
    vmLaunch {
      _favoriteRepository.addOrRemove(state.mapId)
    }
  }

  private suspend fun syncMap(mapId: String) {
    _loader.load {
      _repository.syncMap(mapId)
    }.onSuccess { data ->
      updateState {
        it.copy(
          mapDate = data.mapDate,
          authors = data.authors,
        )
      }
      _imageRepository.load(mapId, data.mapImage)
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

    // Sync map
    vmLaunch {
      _mapIdFlow.collectLatest { data ->
        syncMap(data)
      }
    }

    // Map info
    vmLaunch {
      _mapIdFlow
        .onEach {
          updateState {
            it.copy(
              mapName = "",
              mapImage = "",
              mapDate = "",
              authors = emptyList(),
            )
          }
        }
        .flatMapLatest { _repository.getMapFlow(it) }
        .filterNotNull()
        .collect { data ->
          updateState {
            it.copy(
              mapName = data.name,
              mapImage = data.image,
            )
          }
        }
    }

    // Map records
    vmLaunch {
      _mapIdFlow
        .onEach { updateState { it.copy(records = emptyList()) } }
        .flatMapLatest { _repository.getMapRecordsFlow(it) }
        .collect { data ->
          updateState {
            it.copy(
              currentRecord = data.firstOrNull(),
              records = data,
            )
          }
        }
    }

    vmLaunch {
      _mapIdFlow.collectLatest { mapId ->
        _favoriteRepository.favoriteStateFlow(mapId)
          .collect { data ->
            updateState {
              it.copy(favorite = data)
            }
          }
      }
    }
  }

  @Immutable
  data class State(
    val mapId: String = "",

    val isLoading: Boolean = false,
    val result: Result<Unit>? = null,

    val mapName: String = "",
    val mapImage: String? = null,
    val mapDate: String? = null,
    val authors: List<UserModel> = emptyList(),

    val currentRecord: RecordModel? = null,
    val records: List<RecordModel> = emptyList(),

    val favorite: Boolean = false,
  )
}