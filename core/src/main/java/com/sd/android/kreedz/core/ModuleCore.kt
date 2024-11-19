package com.sd.android.kreedz.core

import android.content.Context
import com.sd.android.kreedz.data.ModuleData
import com.sd.lib.xlog.FLog
import com.sd.lib.xlog.FLogLevel
import com.sd.lib.xlog.fLogDir

object ModuleCore {
   fun init(
      context: Context,
      isRelease: Boolean,
   ) {
      initLog(context, isRelease)
      ModuleData.init(context, isRelease)
   }
}

private fun initLog(
   context: Context,
   isRelease: Boolean,
) {
   FLog.init(
      directory = context.fLogDir()
   ).also { init ->
      if (init) {
         FLog.setLimitMBPerDay(20)
         FLog.deleteLog(3)
         if (isRelease) {
            FLog.setLevel(FLogLevel.Warning)
            FLog.setConsoleLogEnabled(false)
         } else {
            FLog.setLevel(FLogLevel.All)
            FLog.setConsoleLogEnabled(true)
         }
      }
   }
}