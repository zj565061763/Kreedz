package com.sd.android.kreedz.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sd.android.kreedz.data.database.entity.MapDao
import com.sd.android.kreedz.data.database.entity.MapEntity
import com.sd.android.kreedz.data.database.entity.RecordDao
import com.sd.android.kreedz.data.database.entity.RecordEntity
import com.sd.android.kreedz.data.database.entity.UserDao
import com.sd.android.kreedz.data.database.entity.UserEntity

@Database(
  entities = [
    MapEntity::class,
    UserEntity::class,
    RecordEntity::class,
  ],
  version = 1,
)
internal abstract class AppDatabase : RoomDatabase() {
  abstract fun mapDao(): MapDao
  abstract fun userDao(): UserDao
  abstract fun recordDao(): RecordDao

  companion object {
    lateinit var db: AppDatabase

    fun init(context: Context) {
      if (!Companion::db.isInitialized) {
        db = Room.databaseBuilder(
          context = context.applicationContext,
          klass = AppDatabase::class.java,
          name = "database"
        ).build()
      }
    }
  }
}