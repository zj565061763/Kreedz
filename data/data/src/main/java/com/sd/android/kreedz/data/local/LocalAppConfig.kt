package com.sd.android.kreedz.data.local

import com.sd.lib.datastore.DatastoreType
import com.sd.lib.datastore.FDatastore
import com.sd.lib.datastore.get
import com.sd.lib.datastore.withDefault
import com.sd.lib.lifecycle.fAppLifecycleScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@DatastoreType("local_app_config")
internal data class LocalAppConfig(
  val isLightMode: Boolean = true,
  val lastLoginUsername: String = "",
) {
  companion object {
    private val _store = FDatastore.get(LocalAppConfig::class.java).withDefault { LocalAppConfig() }

    val isLightModeFlow: StateFlow<Boolean> = _store.flow
      .map { it.isLightMode }
      .stateIn(
        scope = fAppLifecycleScope,
        started = SharingStarted.Lazily,
        initialValue = true,
      )

    suspend fun setLastLoginUsername(username: String) {
      _store.update { it.copy(lastLoginUsername = username) }
    }

    suspend fun getLastLoginUsername(): String {
      return _store.get().lastLoginUsername
    }

    suspend fun toggleLightMode() {
      _store.update {
        it.copy(isLightMode = !it.isLightMode)
      }
    }
  }
}