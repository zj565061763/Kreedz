package com.sd.android.kreedz.data.database.entity

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.sd.android.kreedz.data.database.ModuleDatabase
import com.sd.lib.xlog.li
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Entity(tableName = "t_user")
data class UserEntity(
  @PrimaryKey
  val id: String,
  val nickname: String,
  val country: String?,
)

interface UserEntityDao {
  suspend fun insertOrUpdate(items: List<UserEntity>)
  suspend fun insertOrIgnore(item: UserEntity)
  fun getById(id: String): Flow<UserEntity?>
  fun getByIds(ids: List<String>): Flow<List<UserEntity>>
}

@Dao
internal interface UserDao : UserEntityDao {
  override suspend fun insertOrUpdate(items: List<UserEntity>) {
    withContext(Dispatchers.IO) {
      items.asSequence()
        .filter { it.id.isNotBlank() && it.nickname.isNotBlank() }
        .distinctBy { it.id }
        .toList()
    }.also { filterItems ->
      ModuleDatabase.li { "User.insertOrUpdate items:${items.size} filter:${filterItems.size}" }
      insertOrUpdateInternal(filterItems)
    }
  }

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  override suspend fun insertOrIgnore(item: UserEntity)

  @Query("SELECT * FROM t_user WHERE id = :id")
  override fun getById(id: String): Flow<UserEntity?>

  @Query("SELECT * FROM t_user WHERE id IN (:ids)")
  override fun getByIds(ids: List<String>): Flow<List<UserEntity>>

  //-------------------- Internal methods start --------------------

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateInternal(items: List<UserEntity>)
}