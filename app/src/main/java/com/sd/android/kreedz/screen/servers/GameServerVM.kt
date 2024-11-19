package com.sd.android.kreedz.screen.servers

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.GameServerModel
import com.sd.android.kreedz.data.repository.GameServerRepository
import com.sd.lib.coroutines.FLoader

class GameServerVM : BaseViewModel<GameServerVM.State, Any>(State()) {
   private val _repository = GameServerRepository()
   private val _loader = FLoader()

   fun init() {
      if (state.servers.isEmpty()) {
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
      val data = _repository.getGameServers()
      updateState {
         it.copy(servers = data)
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
   }

   @Immutable
   data class State(
      val isLoading: Boolean = false,
      val servers: List<GameServerModel> = emptyList(),
   )
}