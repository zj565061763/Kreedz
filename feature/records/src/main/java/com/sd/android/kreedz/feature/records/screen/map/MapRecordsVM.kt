package com.sd.android.kreedz.feature.records.screen.map

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.snapshotFlow
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.MapRecordModel
import com.sd.android.kreedz.data.repository.MapRecordRepository
import com.sd.lib.coroutines.FLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

@OptIn(FlowPreview::class)
class MapRecordsVM : BaseViewModel<MapRecordsVM.State, Any>(State()) {
  private val _repository = MapRecordRepository()

  private val _initLoader = FLoader()
  private val _refreshLoader = FLoader()

  val textFieldState = TextFieldState()

  fun refresh() {
    vmLaunch {
      _initLoader.cancel()
      _refreshLoader.load {
        _repository.sync()
      }.onFailure { error ->
        sendEffect(error)
      }
    }
  }

  fun toggleSortsVisibility() {
    updateState {
      it.copy(showSorts = !it.showSorts)
    }
  }

  fun clickSort(type: MapRecordsSortType) {
    updateState {
      val newSort = it.currentSort.newSort(type)
      it.copy(currentSort = newSort)
    }
  }

  private suspend fun filterList(
    list: List<MapRecordModel>,
    keywords: List<String>,
  ): List<MapRecordModel> {
    if (keywords.isEmpty()) return list
    return withContext(Dispatchers.IO) {
      list.filter { item ->
        yield()
        keywords.matchContent(item.map.name) ||
          keywords.matchContent(item.record?.player?.nickname)
      }
    }
  }

  private suspend fun sortList(
    list: List<MapRecordModel>,
    sort: MapRecordsSortModel,
  ): List<MapRecordModel> {
    return withContext(Dispatchers.IO) {
      if (sort.asc) {
        when (sort.type) {
          MapRecordsSortType.Map -> list.sortedBy { it.map.name }
          MapRecordsSortType.Jumper -> list.sortedBy { it.record?.player?.nickname }
          MapRecordsSortType.Time -> list.sortedBy { it.record?.time ?: Long.MAX_VALUE }
          MapRecordsSortType.Date -> list.sortedBy { it.record?.date ?: Long.MAX_VALUE }
        }
      } else {
        when (sort.type) {
          MapRecordsSortType.Map -> list.sortedByDescending { it.map.name }
          MapRecordsSortType.Jumper -> list.sortedByDescending { it.record?.player?.nickname }
          MapRecordsSortType.Time -> list.sortedByDescending { it.record?.time ?: Long.MAX_VALUE }
          MapRecordsSortType.Date -> list.sortedByDescending { it.record?.date ?: Long.MAX_VALUE }
        }
      }
    }
  }

  init {
    val keywordsFlow = snapshotFlow { textFieldState.text }
      .debounce(400)
      .map { it.trim().split(" ") }
      .map { data -> data.filter { it.isNotBlank() }.distinct() }
      .flowOn(Dispatchers.IO)

    val sortFlow = stateFlow.map { it.currentSort }.distinctUntilChanged()

    // Filter data
    vmLaunch {
      _repository.getMapRecordFlow()
        .combine(keywordsFlow) { data, keywords ->
          val list = filterList(data, keywords)
          KeywordsResult(keywords, list)
        }
        .combine(sortFlow) { data, sort ->
          val list = sortList(data.records, sort)
          KeywordsResult(data.keywords, list)
        }
        .collect { data ->
          updateState {
            it.copy(
              keywords = data.keywords,
              records = data.records,
            )
          }
        }
    }

    // Sync loading state
    vmLaunch {
      _initLoader.loadingFlow.collect { data ->
        updateState {
          it.copy(isLoading = data)
        }
      }
    }

    // Sync refreshing state
    vmLaunch {
      _refreshLoader.loadingFlow.collect { data ->
        updateState {
          it.copy(isRefreshing = data)
        }
      }
    }

    // Initial sync
    vmLaunch {
      _initLoader.load {
        _repository.syncAtLeastOnce()
      }.onFailure { error ->
        sendEffect(error)
      }
    }
  }

  @Immutable
  data class State(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,

    val keywords: List<String> = emptyList(),
    val records: List<MapRecordModel> = emptyList(),

    val showSorts: Boolean = false,
    val currentSort: MapRecordsSortModel = MapRecordsSortModel(
      type = MapRecordsSortType.Map,
      asc = true,
    ),
  )
}

private fun MapRecordsSortModel.newSort(newType: MapRecordsSortType): MapRecordsSortModel {
  if (type != newType) {
    val newAsc = when (newType) {
      MapRecordsSortType.Date -> false
      else -> true
    }
    return MapRecordsSortModel(type = newType, asc = newAsc)
  }
  return copy(asc = !asc)
}

private fun List<String>.matchContent(content: String?): Boolean {
  if (content.isNullOrBlank()) return false
  val lowerCaseContent = content.lowercase()
  return all { lowerCaseContent.contains(it) }
}

private data class KeywordsResult(
  val keywords: List<String>,
  val records: List<MapRecordModel>,
)