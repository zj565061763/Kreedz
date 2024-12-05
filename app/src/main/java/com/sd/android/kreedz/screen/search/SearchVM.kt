package com.sd.android.kreedz.screen.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.SearchNewsModel
import com.sd.android.kreedz.data.model.SearchUserModel
import com.sd.android.kreedz.data.repository.SearchRepository
import com.sd.lib.coroutines.FLoader
import com.sd.lib.coroutines.tryLoad
import com.sd.lib.retry.ktx.fNetRetry
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(FlowPreview::class)
class SearchVM : BaseViewModel<SearchVM.State, Any>(State()) {
  private val _repository = SearchRepository()
  private val _loader = FLoader()

  val inputState = TextFieldState()

  fun clickSearch() {
    vmLaunch {
      _loader.tryLoad {
        searchKeyword(state.keyword)
      }
    }
  }

  private fun search() {
    vmLaunch {
      _loader.load {
        searchKeyword(state.keyword)
      }
    }
  }

  private suspend fun searchKeyword(keyword: String) {
    if (keyword.isEmpty()) return
    fNetRetry {
      _repository.search(keyword)
    }.onSuccess { data ->
      updateState {
        it.copy(
          listUser = data.listUser,
          listNews = data.listNews,
        )
      }
    }.onFailure { error ->
      sendEffect(error)
    }
  }

  init {
    vmLaunch {
      _loader.loadingFlow.collect { data ->
        updateState {
          it.copy(isSearching = data)
        }
      }
    }

    vmLaunch {
      snapshotFlow { inputState.text }
        .map { keyword ->
          if (keyword.length < 3 || keyword.isBlank()) {
            ""
          } else {
            keyword
          }
        }
        .distinctUntilChanged()
        .onEach { keyword ->
          _loader.cancel()
          updateState { State(keyword = keyword.toString()) }
        }
        .debounce(1_000)
        .collect { search() }
    }
  }

  data class State(
    val keyword: String = "",
    val isSearching: Boolean = false,
    val listUser: List<SearchUserModel>? = null,
    val listNews: List<SearchNewsModel>? = null,
  )
}