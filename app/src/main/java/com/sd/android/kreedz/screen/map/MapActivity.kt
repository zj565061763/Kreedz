package com.sd.android.kreedz.screen.map

import android.os.Bundle
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.MAP)
class MapActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val id = intent.getStringExtra("id")
    if (id.isNullOrBlank()) {
      finish()
      return
    }

    setPageContent {
      MapScreen(
        mapId = id,
        onClickBack = { finish() },
      )
    }
  }
}