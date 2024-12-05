package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.database.entity.MapEntity
import com.sd.android.kreedz.data.database.entity.RecordEntity
import com.sd.android.kreedz.data.database.entity.UserEntity
import com.sd.android.kreedz.data.model.MapRecordModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetRecord
import com.sd.android.kreedz.data.repository.dao.DaoMapRepository
import com.sd.android.kreedz.data.repository.dao.DaoRecordRepository
import com.sd.android.kreedz.data.repository.dao.DaoUserRepository
import com.sd.lib.coroutines.FSyncable
import com.sd.lib.coroutines.syncOrThrow
import com.sd.lib.lifecycle.fAppLifecycleScope
import com.sd.lib.retry.ktx.fNetRetry
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.li
import com.sd.lib.xlog.lw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

fun MapRecordRepository(): MapRecordRepository = MapRecordRepositoryImpl

interface MapRecordRepository {
  fun getMapRecordFlow(): Flow<List<MapRecordModel>>
  suspend fun sync()
  suspend fun syncAtLeastOnce()
}

private object MapRecordRepositoryImpl : MapRecordRepository, FLogger {
  private val _datasource = NetDataSource()

  private val _daoMap = DaoMapRepository()
  private val _daoUser = DaoUserRepository()
  private val _daoRecord = DaoRecordRepository()

  private val _mapRecordsFlow = _daoMap.getAllWithRecord()
    .stateIn(
      scope = fAppLifecycleScope,
      started = SharingStarted.Lazily,
      initialValue = emptyList(),
    )

  private val _syncable = FSyncable {
    li { "sync" }
    runCatching { doSync() }
      .onSuccess { li { "sync onSuccess" } }
      .onFailure { lw { "sync onFailure ${it.stackTraceToString()}" } }
      .getOrThrow()
  }

  private var _hasSyncSuccess = false

  override fun getMapRecordFlow(): Flow<List<MapRecordModel>> {
    return _mapRecordsFlow
  }

  override suspend fun sync() {
    _syncable.syncOrThrow()
  }

  override suspend fun syncAtLeastOnce() {
    if (!_hasSyncSuccess) {
      fNetRetry { sync() }.getOrThrow()
    }
  }

  private suspend fun doSync() {
    val list = _datasource.getRecords(0)
    supervisorScope {
      launch { saveMaps(list) }
      launch { saveUsers(list) }
      launch { saveRecords(list) }
    }
    _hasSyncSuccess = true
  }

  private suspend fun saveMaps(list: List<NetRecord>) {
    withContext(Dispatchers.IO) {
      list.map { it.asMapEntity() }
    }.also {
      _daoMap.insertOrUpdateRecordId(it)
    }
  }

  private suspend fun saveUsers(list: List<NetRecord>) {
    withContext(Dispatchers.IO) {
      list.map { it.asUserEntity() }
    }.also {
      _daoUser.insertOrUpdate(it)
    }
  }

  private suspend fun saveRecords(list: List<NetRecord>) {
    withContext(Dispatchers.IO) {
      list.mapNotNull { it.asRecordEntity() }
    }.also {
      _daoRecord.insertOrUpdate(it)
    }
  }
}

private fun NetRecord.asMapEntity(): MapEntity {
  return MapEntity(
    id = mapId,
    name = mapName,
    recordId = demoId,
    image = null,
  )
}

private fun NetRecord.asUserEntity(): UserEntity {
  return UserEntity(
    id = playerId ?: "",
    nickname = playerName ?: "",
    country = country,
  )
}

private fun NetRecord.asRecordEntity(): RecordEntity? {
  val id = demoId
  if (id.isNullOrBlank()) return null
  return RecordEntity(
    id = id,
    mapId = mapId,
    userId = playerId ?: "",
    userNickname = playerName ?: "",
    userCountry = country,
    time = time,
    date = releaseDate,
    youtubeLink = youtubeLink,
    deleted = false,
  )
}