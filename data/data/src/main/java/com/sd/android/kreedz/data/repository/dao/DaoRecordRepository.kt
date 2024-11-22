package com.sd.android.kreedz.data.repository.dao

import com.sd.android.kreedz.data.database.ModuleDatabase
import com.sd.android.kreedz.data.database.entity.RecordEntity
import com.sd.android.kreedz.data.database.entity.RecordEntityWithMapAndUser
import com.sd.android.kreedz.data.mapper.asMapModel
import com.sd.android.kreedz.data.mapper.asRecordModel
import com.sd.android.kreedz.data.mapper.asUserModel
import com.sd.android.kreedz.data.model.RecordModel
import com.sd.android.kreedz.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal fun DaoRecordRepository(): DaoRecordRepository = DaoRecordRepositoryImpl

internal interface DaoRecordRepository {
   fun getByMapId(mapId: String, limit: Int): Flow<List<RecordModel>>
   fun getByIds(ids: List<String>): Flow<List<RecordModel>>
   fun getPrevious(id: String): Flow<RecordModel?>
   suspend fun insertOrUpdate(items: List<RecordEntity>)
   suspend fun insertOrIgnore(items: List<RecordEntity>)
}

private object DaoRecordRepositoryImpl : DaoRecordRepository {
   private val _recordDao = ModuleDatabase.recordDao()

   override fun getByMapId(mapId: String, limit: Int): Flow<List<RecordModel>> {
      return _recordDao.getByMapId(mapId = mapId, limit = limit)
         .map { it.mapNotNull(RecordEntityWithMapAndUser::asRecordModel) }
         .flowOn(Dispatchers.IO)
   }

   override fun getByIds(ids: List<String>): Flow<List<RecordModel>> {
      return _recordDao.getByIds(ids)
         .map { data ->
            data.mapNotNull { it.asRecordModel() }
               .sortedBy { it.map.name }
         }.flowOn(Dispatchers.IO)
   }

   override fun getPrevious(id: String): Flow<RecordModel?> {
      return _recordDao.getPrevious(id)
         .distinctUntilChanged()
         .map { it?.asRecordModel() }
         .flowOn(Dispatchers.IO)
   }

   override suspend fun insertOrUpdate(items: List<RecordEntity>) {
      _recordDao.insertOrUpdate(items)
   }

   override suspend fun insertOrIgnore(items: List<RecordEntity>) {
      _recordDao.insertOrIgnore(items)
   }
}

private fun RecordEntityWithMapAndUser.asRecordModel(): RecordModel? {
   val map = map?.asMapModel() ?: return null
   val player = user?.asUserModel() ?: UserModel(
      id = record.userId,
      nickname = record.userNickname,
      country = record.userCountry,
   )
   return record.asRecordModel(
      map = map,
      player = player,
   )
}