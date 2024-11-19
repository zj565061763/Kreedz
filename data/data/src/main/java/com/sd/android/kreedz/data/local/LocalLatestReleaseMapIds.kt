package com.sd.android.kreedz.data.local

import com.sd.lib.datastore.DatastoreType
import com.sd.lib.datastore.FDatastore
import com.sd.lib.datastore.get
import com.sd.lib.datastore.update

@DatastoreType("local_latest_release_map_ids")
internal data class LocalLatestReleaseMapIds(
   val newsId: String,
   val mapIds: List<String>,
) {
   companion object {
      private val _store = FDatastore.get(LocalLatestReleaseMapIds::class.java)

      suspend fun replace(newsId: String) {
         _store.replace { data ->
            if (data?.newsId != newsId) {
               LocalLatestReleaseMapIds(newsId = newsId, mapIds = emptyList())
            } else data
         }
      }

      suspend fun has(mapId: String): Boolean {
         return _store.get()?.mapIds?.contains(mapId) == true
      }

      suspend fun add(mapId: String) {
         if (mapId.isBlank()) return
         _store.update {
            it.copy(mapIds = it.mapIds + mapId)
         }
      }
   }
}