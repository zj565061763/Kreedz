package com.sd.android.kreedz.data.repository.dao

import com.sd.android.kreedz.data.database.ModuleDatabase
import com.sd.android.kreedz.data.database.entity.MapEntity
import com.sd.android.kreedz.data.database.entity.RecordEntity
import com.sd.android.kreedz.data.database.entity.UserEntity
import com.sd.android.kreedz.data.mapper.asMapModel
import com.sd.android.kreedz.data.mapper.asRecordModel
import com.sd.android.kreedz.data.mapper.asUserModel
import com.sd.android.kreedz.data.model.MapModel
import com.sd.android.kreedz.data.model.MapRecordModel
import com.sd.android.kreedz.data.model.RecordModel
import com.sd.android.kreedz.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

internal fun DaoMapRepository(): DaoMapRepository = DaoMapRepositoryImpl

internal interface DaoMapRepository {
   fun getById(id: String): Flow<MapModel?>
   fun getAllWithRecord(): Flow<List<MapRecordModel>>
   suspend fun insertOrUpdateRecordId(items: List<MapEntity>)
   suspend fun insertOrUpdateWithoutImage(item: MapEntity)
   suspend fun insertOrIgnore(items: List<MapEntity>)
   suspend fun updateImage(id: String, image: String)
}

private object DaoMapRepositoryImpl : DaoMapRepository {
   private val _mapDao = ModuleDatabase.mapDao()

   override fun getById(id: String): Flow<MapModel?> {
      return _mapDao.getById(id)
         .distinctUntilChanged()
         .map { it?.asMapModel() }
         .flowOn(Dispatchers.IO)
   }

   override fun getAllWithRecord(): Flow<List<MapRecordModel>> {
      return _mapDao.getAllWithRecordAndUser()
         .mapNotNull { data -> data.map { it.asMapRecordModel() } }
         .flowOn(Dispatchers.IO)
   }

   override suspend fun insertOrUpdateRecordId(items: List<MapEntity>) {
      _mapDao.insertOrUpdateRecordId(items)
   }

   override suspend fun insertOrUpdateWithoutImage(item: MapEntity) {
      _mapDao.insertOrUpdateWithoutImage(item)
   }

   override suspend fun insertOrIgnore(items: List<MapEntity>) {
      _mapDao.insertOrIgnore(items)
   }

   override suspend fun updateImage(id: String, image: String) {
      _mapDao.updateImage(id = id, image = image)
   }
}

private fun Map.Entry<MapEntity, Map<RecordEntity, UserEntity?>>.asMapRecordModel(): MapRecordModel {
   val mapModel = key.asMapModel()
   var recordModel: RecordModel? = null

   if (value.isNotEmpty()) {
      check(value.size == 1)
      val (record, user) = value.entries.first()
      recordModel = record.asRecordModel(
         map = mapModel,
         player = user?.asUserModel() ?: UserModel(
            id = record.userId,
            nickname = record.userNickname,
            country = record.userCountry,
         )
      )
   }

   return MapRecordModel(
      map = mapModel,
      record = recordModel,
   )
}
