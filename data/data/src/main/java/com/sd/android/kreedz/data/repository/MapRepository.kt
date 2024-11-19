package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.database.entity.MapEntity
import com.sd.android.kreedz.data.database.entity.RecordEntity
import com.sd.android.kreedz.data.database.entity.UserEntity
import com.sd.android.kreedz.data.model.MapDetailsModel
import com.sd.android.kreedz.data.model.MapModel
import com.sd.android.kreedz.data.model.RecordModel
import com.sd.android.kreedz.data.model.UserModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetMap
import com.sd.android.kreedz.data.network.model.NetMapRecord
import com.sd.android.kreedz.data.repository.dao.DaoMapRepository
import com.sd.android.kreedz.data.repository.dao.DaoRecordRepository
import com.sd.android.kreedz.data.repository.dao.DaoUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

fun MapRepository(): MapRepository = MapRepositoryImpl

interface MapRepository {
   fun getMapFlow(id: String): Flow<MapModel?>
   fun getMapRecordsFlow(id: String): Flow<List<RecordModel>>

   suspend fun syncMap(id: String): MapDetailsModel
}

private object MapRepositoryImpl : MapRepository {
   private val _datasource = NetDataSource()

   private val _daoMap = DaoMapRepository()
   private val _daoUser = DaoUserRepository()
   private val _daoRecord = DaoRecordRepository()

   override fun getMapFlow(id: String): Flow<MapModel?> {
      return _daoMap.getById(id)
   }

   override fun getMapRecordsFlow(id: String): Flow<List<RecordModel>> {
      return _daoRecord.getByMapId(mapId = id, limit = 999)
   }

   override suspend fun syncMap(id: String): MapDetailsModel {
      require(id.isNotBlank())
      val data = _datasource.getMap(id)

      supervisorScope {
         launch { saveMap(data, id) }
         launch { saveUser(data) }
         launch { saveRecord(data) }
      }

      return MapDetailsModel(
         mapImage = data.mapImage,
         mapDate = data.mapReleaseDate,
         authors = data.asAuthors(),
      )
   }

   private suspend fun saveMap(data: NetMap, id: String) {
      withContext(Dispatchers.IO) {
         with(data) {
            MapEntity(
               id = id,
               name = mapName,
               recordId = currentRecord?.demoId,
               image = mapImage,
            )
         }
      }.also {
         _daoMap.insertOrUpdateWithoutImage(it)
      }
   }

   private suspend fun saveUser(data: NetMap) {
      withContext(Dispatchers.IO) {
         (data.listRecords ?: emptyList()).map {
            UserEntity(
               id = it.playerId ?: "",
               nickname = it.playerName,
               country = it.country,
            )
         }
      }.also {
         _daoUser.insertOrUpdate(it)
      }
   }

   private suspend fun saveRecord(data: NetMap) {
      withContext(Dispatchers.IO) {
         with(data) {
            val current = currentRecord?.asRecordEntity()
            val list = listRecords?.map { it.asRecordEntity() } ?: emptyList()
            if (current != null) list + current else list
         }
      }.also {
         _daoRecord.insertOrUpdate(it)
      }
   }
}

private fun NetMap.asAuthors(): List<UserModel> {
   return authors?.map {
      UserModel(
         id = it.mapAuthorId ?: "",
         nickname = it.mapperName,
         country = it.mapperCountry,
      )
   } ?: emptyList()
}

private fun NetMapRecord.asRecordEntity(): RecordEntity {
   return RecordEntity(
      id = demoId,
      mapId = mapId,
      userId = playerId ?: "",
      userNickname = playerName,
      userCountry = country,
      time = time,
      date = releaseDate,
      youtubeLink = youtubeLink,
      deleted = isDeleted,
   )
}