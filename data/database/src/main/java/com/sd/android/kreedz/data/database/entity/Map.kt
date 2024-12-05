package com.sd.android.kreedz.data.database.entity

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sd.android.kreedz.data.database.ModuleDatabase
import com.sd.lib.xlog.li
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Entity(tableName = "t_map")
data class MapEntity(
  @PrimaryKey
  val id: String,
  val name: String,
  val recordId: String?,
  val image: String?,
)

interface MapEntityDao {
  suspend fun insertOrUpdateRecordId(items: List<MapEntity>)
  suspend fun insertOrUpdateWithoutImage(item: MapEntity)
  suspend fun insertOrIgnore(items: List<MapEntity>)
  suspend fun updateImage(id: String, image: String)
  fun getById(id: String): Flow<MapEntity?>
  fun getAllWithRecordAndUser(): Flow<Map<MapEntity, Map<RecordEntity, UserEntity?>>>
}

@Dao
internal interface MapDao : MapEntityDao {
  @Transaction
  override suspend fun insertOrUpdateRecordId(items: List<MapEntity>) {
    val filterItems = withContext(Dispatchers.IO) {
      items.filter {
        it.id.isNotBlank() && it.name.isNotBlank()
      }
    }

    ModuleDatabase.li { "Map.insertOrUpdateRecordId items:${items.size} filter:${filterItems.size}" }
    insertOrIgnoreInternal(filterItems)

    withContext(Dispatchers.IO) {
      filterItems.map { item ->
        with(item) {
          MapEntityUpdateRecordId(
            id = id,
            name = name,
            recordId = recordId,
          )
        }
      }
    }.also {
      updateWithRecordIdInternal(it)
    }
  }

  @Transaction
  override suspend fun insertOrUpdateWithoutImage(item: MapEntity) {
    if (item.id.isBlank()) return
    if (item.name.isBlank()) return

    ModuleDatabase.li { "Map.insertOrUpdateWithoutImage (${item.id}|${item.name}|${item.recordId})" }
    insertOrIgnoreInternal(listOf(item))

    with(item) {
      MapEntityWithoutImage(
        id = id,
        name = name,
        recordId = recordId,
      )
    }.also {
      updateWithoutImageInternal(it)
    }
  }

  override suspend fun insertOrIgnore(items: List<MapEntity>) {
    withContext(Dispatchers.IO) {
      items.filter {
        it.id.isNotBlank() && it.name.isNotBlank()
      }
    }.also { filterItems ->
      ModuleDatabase.li { "Map.insertOrIgnore items:${items.size} filter:${filterItems.size}" }
      insertOrIgnoreInternal(filterItems)
    }
  }

  @Query("UPDATE t_map SET image = :image WHERE id = :id")
  override suspend fun updateImage(id: String, image: String)

  @Query("SELECT * FROM t_map WHERE id = :id")
  override fun getById(id: String): Flow<MapEntity?>

  @Query(
    """
      SELECT * FROM t_map
      LEFT JOIN t_record ON t_map.recordId = t_record.id
      LEFT JOIN t_user ON t_record.userId = t_user.id
   """
  )
  override fun getAllWithRecordAndUser(): Flow<Map<MapEntity, Map<RecordEntity, UserEntity?>>>

  //-------------------- Internal methods start --------------------

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertOrIgnoreInternal(items: List<MapEntity>)

  @Update(
    entity = MapEntity::class,
    onConflict = OnConflictStrategy.REPLACE,
  )
  suspend fun updateWithRecordIdInternal(items: List<MapEntityUpdateRecordId>)

  @Update(
    entity = MapEntity::class,
    onConflict = OnConflictStrategy.REPLACE,
  )
  suspend fun updateWithoutImageInternal(item: MapEntityWithoutImage)
}

internal data class MapEntityUpdateRecordId(
  val id: String,
  val name: String,
  val recordId: String?,
)

internal data class MapEntityWithoutImage(
  val id: String,
  val name: String,
  val recordId: String?,
)