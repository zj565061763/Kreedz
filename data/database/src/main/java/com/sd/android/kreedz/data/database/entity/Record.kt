package com.sd.android.kreedz.data.database.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.sd.android.kreedz.data.database.ModuleDatabase
import com.sd.lib.xlog.li
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Entity(tableName = "t_record")
data class RecordEntity(
  @PrimaryKey
  val id: String,
  val mapId: String,

  val userId: String,
  val userNickname: String,
  val userCountry: String?,

  val time: Long,
  val date: Long,
  val youtubeLink: String?,
  val deleted: Boolean,
)

data class RecordEntityWithMapAndUser(
  @Embedded
  val record: RecordEntity,

  @Relation(
    parentColumn = "mapId",
    entityColumn = "id"
  )
  val map: MapEntity?,

  @Relation(
    parentColumn = "userId",
    entityColumn = "id"
  )
  val user: UserEntity?,
)

interface RecordEntityDao {
  suspend fun insertOrUpdate(items: List<RecordEntity>)
  suspend fun insertOrIgnore(items: List<RecordEntity>)
  fun getByMapId(mapId: String, limit: Int): Flow<List<RecordEntityWithMapAndUser>>
  fun getByIds(ids: List<String>): Flow<List<RecordEntityWithMapAndUser>>
  fun getPrevious(id: String): Flow<RecordEntityWithMapAndUser?>
}

@OptIn(ExperimentalCoroutinesApi::class)
@Dao
internal interface RecordDao : RecordEntityDao {
  override suspend fun insertOrUpdate(items: List<RecordEntity>) {
    filterItems(items).also { filterItems ->
      ModuleDatabase.li { "Record.insertOrUpdate items:${items.size} filter:${filterItems.size}" }
      insertOrUpdateInternal(filterItems)
    }
  }

  override suspend fun insertOrIgnore(items: List<RecordEntity>) {
    filterItems(items).also { filterItems ->
      ModuleDatabase.li { "Record.insertOrIgnore items:${items.size} filter:${filterItems.size}" }
      insertOrIgnoreInternal(filterItems)
    }
  }

  @Transaction
  @Query("SELECT * FROM t_record WHERE mapId = :mapId ORDER BY date DESC LIMIT :limit")
  override fun getByMapId(mapId: String, limit: Int): Flow<List<RecordEntityWithMapAndUser>>

  @Transaction
  @Query("SELECT * FROM t_record WHERE id IN (:ids)")
  override fun getByIds(ids: List<String>): Flow<List<RecordEntityWithMapAndUser>>

  override fun getPrevious(id: String): Flow<RecordEntityWithMapAndUser?> {
    return getByIdInternal(id)
      .flatMapLatest {
        val mapId = it?.record?.mapId ?: ""
        val date = it?.record?.date ?: 0
        getPreviousInternal(mapId = mapId, date = date)
      }
  }

  //-------------------- Internal methods start --------------------

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateInternal(items: List<RecordEntity>)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertOrIgnoreInternal(items: List<RecordEntity>)

  @Transaction
  @Query("SELECT * FROM t_record WHERE mapId = :mapId AND date < :date ORDER BY date DESC LIMIT 1")
  fun getPreviousInternal(mapId: String, date: Long): Flow<RecordEntityWithMapAndUser?>

  private fun getByIdInternal(id: String): Flow<RecordEntityWithMapAndUser?> {
    return getByIds(listOf(id))
      .map { it.firstOrNull() }
      .distinctUntilChanged()
  }
}

private suspend fun filterItems(items: List<RecordEntity>): List<RecordEntity> {
  return withContext(Dispatchers.IO) {
    items.filter {
      it.id.isNotBlank()
        && it.mapId.isNotBlank()
        && (it.userId.isNotBlank() || it.userNickname.isNotBlank())
    }
  }
}