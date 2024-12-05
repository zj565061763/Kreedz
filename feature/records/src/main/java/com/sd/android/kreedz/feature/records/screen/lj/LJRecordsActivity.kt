package com.sd.android.kreedz.feature.records.screen.lj

import androidx.compose.runtime.Composable
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.LJ_RECORDS)
internal class LJRecordsActivity : BaseActivity() {
  @Composable
  override fun ContentImpl() {
    LJRecordsScreen(
      onClickBack = {
        finish()
      }
    )
  }
}