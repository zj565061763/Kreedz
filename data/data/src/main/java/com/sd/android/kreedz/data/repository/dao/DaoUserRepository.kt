package com.sd.android.kreedz.data.repository.dao

import com.sd.android.kreedz.data.database.ModuleDatabase
import com.sd.android.kreedz.data.database.entity.UserEntity
import com.sd.android.kreedz.data.mapper.asUserModel
import com.sd.android.kreedz.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal fun DaoUserRepository(): DaoUserRepository = DaoUserRepositoryImpl

internal interface DaoUserRepository {
  fun getById(id: String): Flow<UserModel?>
  fun getByIds(ids: List<String>): Flow<List<UserModel>>
  suspend fun insertOrUpdate(items: List<UserEntity>)
  suspend fun insertOrIgnore(item: UserEntity)
}

private object DaoUserRepositoryImpl : DaoUserRepository {
  private val _userDao = ModuleDatabase.userDao()

  override fun getById(id: String): Flow<UserModel?> {
    return _userDao.getById(id)
      .distinctUntilChanged()
      .map { it?.asUserModel() }
      .flowOn(Dispatchers.IO)
  }

  override fun getByIds(ids: List<String>): Flow<List<UserModel>> {
    return _userDao.getByIds(ids)
      .map { it.map(UserEntity::asUserModel) }
      .flowOn(Dispatchers.IO)
  }

  override suspend fun insertOrUpdate(items: List<UserEntity>) {
    _userDao.insertOrUpdate(items)
  }

  override suspend fun insertOrIgnore(item: UserEntity) {
    _userDao.insertOrIgnore(item)
  }
}