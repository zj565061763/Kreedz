package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.database.entity.UserEntity
import com.sd.android.kreedz.data.local.LocalLatestReleaseMapIds
import com.sd.android.kreedz.data.model.LatestRecordGroupModel
import com.sd.android.kreedz.data.model.LatestRecordModel
import com.sd.android.kreedz.data.model.LatestReleaseModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetLatestRelease
import com.sd.android.kreedz.data.repository.dao.DaoRecordRepository
import com.sd.android.kreedz.data.repository.dao.DaoUserRepository
import com.sd.lib.coroutines.FLoader
import com.sd.lib.coroutines.FSyncable
import com.sd.lib.coroutines.fGlobalLaunch
import com.sd.lib.coroutines.syncOrThrow
import com.sd.lib.retry.ktx.fNetRetry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

fun LatestReleaseRepository(): LatestReleaseRepository = LatestReleaseRepositoryImpl

interface LatestReleaseRepository {
   suspend fun getLatestReleaseFlow(): Flow<LatestReleaseModel>
   suspend fun sync()
}

@OptIn(ExperimentalCoroutinesApi::class)
private object LatestReleaseRepositoryImpl : LatestReleaseRepository {
   private val _netDataSource = NetDataSource()
   private val _mapLoader = MapLoader()

   private val _mapRecordRepository = MapRecordRepository()
   private val _daoUser = DaoUserRepository()
   private val _daoRecord = DaoRecordRepository()

   private val _sourceFlow = MutableStateFlow<NetLatestRelease?>(null)
   private val _recordsFlow: Flow<Pair<NetLatestRelease, List<LatestRecordModel>>> = _sourceFlow
      .filterNotNull()
      .map { it to it.asRecordIds() }
      .flatMapLatest { pair ->
         val (source, ids) = pair
         _daoRecord.getByIds(ids)
            .map { records ->
               records.map { current ->
                  _daoRecord.getPrevious(current.id)
                     .map { previous ->
                        LatestRecordModel(
                           current = current,
                           previous = previous,
                        )
                     }
               }
            }
            .flatMapLatest { flows ->
               combine(flows) { items ->
                  items.toList()
               }
            }
            .map { source to it }
      }.flowOn(Dispatchers.IO)

   override suspend fun getLatestReleaseFlow(): Flow<LatestReleaseModel> {
      return _recordsFlow
         .map { pair ->
            val (source, groups) = pair
            groups.groupBy { it.current.player }
               .map { map ->
                  LatestRecordGroupModel(
                     player = map.key,
                     records = map.value.sortedBy { it.current.map.name },
                  )
               }
               .sortedBy { item ->
                  source.demosByPlayer.indexOfFirst { it.playerId == item.player.id }
               }
               .let { source to it }
         }
         .map { pair ->
            val (source, groups) = pair
            LatestReleaseModel(
               newsId = source.newsId,
               newsName = source.newsName,
               records = groups,
            )
         }.flowOn(Dispatchers.IO)
   }

   override suspend fun sync() {
      _syncable.syncOrThrow()
   }

   private val _syncable = FSyncable {
      val data = _netDataSource.getLatestRelease()

      // Save users
      withContext(Dispatchers.IO) {
         data.asUserEntities()
      }.also {
         _daoUser.insertOrUpdate(it)
      }

      withContext(Dispatchers.IO) {
         data.asRecordIds()
      }.also { recordIds ->
         syncMapRecords(recordIds)
      }

      _sourceFlow.value = data

      // Load maps
      withContext(Dispatchers.IO) {
         data.asMapIds()
      }.also { mapIds ->
         _mapLoader.load(data.newsId, mapIds)
      }
   }

   private suspend fun syncMapRecords(recordIds: List<String>) {
      val list = _daoRecord.getByIds(recordIds).first()
      if (list.size != recordIds.size) {
         _mapRecordRepository.sync()
      }
   }
}

private class MapLoader {
   private val _repository = MapRepository()
   private val _loader = FLoader()

   fun load(newsId: String?, maps: List<String>) {
      fGlobalLaunch {
         _loader.load {
            loadMaps(newsId, maps)
         }
      }
   }

   private suspend fun loadMaps(newsId: String?, mapIds: List<String>) {
      if (newsId.isNullOrBlank()) {
         // Do not load map when newsId is empty.
         return
      }

      // Replace data if newsId changed.
      LocalLatestReleaseMapIds.replace(newsId)

      for (id in mapIds) {
         if (LocalLatestReleaseMapIds.has(id)) {
            // Skip if the map has loaded.
            continue
         }

         fNetRetry {
            _repository.syncMap(id)
         }.onSuccess {
            // Save loaded map.
            LocalLatestReleaseMapIds.add(id)
         }

         delay(1_000)
      }
   }
}

private fun NetLatestRelease.asUserEntities(): List<UserEntity> {
   return demosByPlayer.map {
      UserEntity(
         id = it.playerId,
         nickname = it.playerName,
         country = it.playerCountry,
      )
   }
}

private fun NetLatestRelease.asRecordIds(): List<String> {
   return demosByPlayer
      .asSequence()
      .flatMap { it.demos }
      .distinctBy { it.demoId }
      .map { it.demoId }
      .toList()
}

private fun NetLatestRelease.asMapIds(): List<String> {
   return demosByPlayer
      .asSequence()
      .flatMap { it.demos }
      .distinctBy { it.mapId }
      .map { it.mapId }
      .toList()
}