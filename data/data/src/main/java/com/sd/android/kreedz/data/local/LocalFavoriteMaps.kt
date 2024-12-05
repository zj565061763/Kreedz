package com.sd.android.kreedz.data.local

import com.sd.lib.datastore.DatastoreType
import com.sd.lib.datastore.FDatastore
import com.sd.lib.datastore.withDefault
import kotlinx.coroutines.flow.Flow

@DatastoreType("local_favorite_maps")
internal data class LocalFavoriteMaps(
  val mapIds: List<String> = emptyList(),
) {
  companion object {
    private val _store = FDatastore.get(LocalFavoriteMaps::class.java).withDefault { LocalFavoriteMaps() }

    val flow: Flow<LocalFavoriteMaps>
      get() = _store.flow

    suspend fun addOrRemove(mapId: String) {
      if (mapId.isBlank()) return
      _store.update { data ->
        val mapIds = data.mapIds.let { mapIds ->
          if (mapIds.contains(mapId)) {
            mapIds - mapId
          } else {
            mapIds.toMutableList().let {
              it.add(0, mapId)
              it.toList()
            }
          }
        }
        data.copy(mapIds = mapIds)
      }
    }
  }
}
