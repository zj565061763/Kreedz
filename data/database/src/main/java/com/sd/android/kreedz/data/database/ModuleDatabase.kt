package com.sd.android.kreedz.data.database

import android.content.Context
import com.sd.android.kreedz.data.database.entity.MapEntityDao
import com.sd.android.kreedz.data.database.entity.RecordEntityDao
import com.sd.android.kreedz.data.database.entity.UserEntityDao
import com.sd.lib.xlog.FLogger

object ModuleDatabase : FLogger {
   fun init(
      context: Context,
      isRelease: Boolean,
   ) {
      AppDatabase.init(context)
   }

   fun mapDao(): MapEntityDao = AppDatabase.db.mapDao()
   fun userDao(): UserEntityDao = AppDatabase.db.userDao()
   fun recordDao(): RecordEntityDao = AppDatabase.db.recordDao()
}