package com.sd.android.kreedz.screen.team

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.TeamRoleModel
import com.sd.android.kreedz.data.repository.TeamRepository
import com.sd.lib.coroutines.FLoader

internal class TeamVM : BaseViewModel<TeamVM.State, Any>(State()) {
  private val _repository = TeamRepository()
  private val _loader = FLoader()

  fun init() {
    if (state.roles.isEmpty()) {
      refresh()
    }
  }

  fun refresh() {
    vmLaunch {
      _loader.load {
        loadData()
      }.onFailure { error ->
        sendEffect(error)
      }
    }
  }

  private suspend fun loadData() {
    val data = _repository.getTeam()
    updateState {
      it.copy(roles = data)
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

    val roles: List<TeamRoleModel> = emptyList(),
  )
}