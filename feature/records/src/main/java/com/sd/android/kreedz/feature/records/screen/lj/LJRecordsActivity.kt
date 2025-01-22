package com.sd.android.kreedz.feature.records.screen.lj

import android.os.Bundle
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.LJ_RECORDS)
internal class LJRecordsActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setPageContent {
      LJRecordsScreen(
        onClickBack = { finish() },
      )
    }
  }
}