package com.sd.android.kreedz.data

import android.content.Context
import com.sd.android.kreedz.data.database.ModuleDatabase
import com.sd.android.kreedz.data.network.ModuleNetwork
import com.sd.lib.coroutines.fGlobalLaunch
import com.sd.lib.datastore.FDatastore
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.lw

object ModuleData : FLogger {
  fun init(
    context: Context,
    isRelease: Boolean,
  ) {
    initDatastore(context)
    ModuleDatabase.init(context, isRelease)
    ModuleNetwork.init(context, isRelease)
  }
}

private fun initDatastore(context: Context) {
  FDatastore.init(context)
  fGlobalLaunch {
    FDatastore.errorFlow.collect { e ->
      ModuleData.lw { "datastore error ${e.stackTraceToString()}" }
    }
  }
}