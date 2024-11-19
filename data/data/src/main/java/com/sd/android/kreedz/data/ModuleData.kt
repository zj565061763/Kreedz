package com.sd.android.kreedz.data

import android.content.Context
import com.sd.android.kreedz.data.database.ModuleDatabase
import com.sd.android.kreedz.data.network.ModuleNetwork
import com.sd.lib.datastore.FDatastore
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.le

object ModuleData : FLogger {
   fun init(
      context: Context,
      isRelease: Boolean,
   ) {
      FDatastore.init(
         context = context,
         onError = { e ->
            ModuleData.le { "datastore error ${e.stackTraceToString()}" }
         },
      )
      ModuleDatabase.init(context, isRelease)
      ModuleNetwork.init(context, isRelease)
   }
}