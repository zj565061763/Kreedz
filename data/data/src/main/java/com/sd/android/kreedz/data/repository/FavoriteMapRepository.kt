package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.local.LocalFavoriteMaps
import com.sd.android.kreedz.data.model.MapRecordModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

fun FavoriteMapRepository(): FavoriteMapRepository = FavoriteMapRepositoryImpl()

interface FavoriteMapRepository {
  fun getFavoriteMapsFlow(): Flow<List<MapRecordModel>>
  fun favoriteStateFlow(mapId: String): Flow<Boolean>
  suspend fun sync()
  suspend fun addOrRemove(mapId: String)
}

private class FavoriteMapRepositoryImpl : FavoriteMapRepository {
  private val _mapRecordRepository = MapRecordRepository()

  override fun getFavoriteMapsFlow(): Flow<List<MapRecordModel>> {
    return LocalFavoriteMaps.flow
      .map { it.mapIds }
      .distinctUntilChanged()
      .combine(_mapRecordRepository.getMapRecordFlow()) { ids, data ->
        ids.mapNotNull { id ->
          data.find { it.map.id == id }
        }
      }
      .flowOn(Dispatchers.IO)
  }

  override fun favoriteStateFlow(mapId: String): Flow<Boolean> {
    return LocalFavoriteMaps.flow
      .map { it.mapIds.contains(mapId) }
      .flowOn(Dispatchers.IO)
  }

  override suspend fun sync() {
    _mapRecordRepository.syncAtLeastOnce()
  }

  override suspend fun addOrRemove(mapId: String) {
    LocalFavoriteMaps.addOrRemove(mapId)
  }
}