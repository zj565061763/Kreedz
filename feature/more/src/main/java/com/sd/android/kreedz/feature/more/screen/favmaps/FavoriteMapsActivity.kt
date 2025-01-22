package com.sd.android.kreedz.feature.more.screen.favmaps

import android.os.Bundle
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.FAVORITE_MAPS)
internal class FavoriteMapsActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setPageContent {
      FavoriteMapsScreen(
        onClickBack = { finish() },
      )
    }
  }
}