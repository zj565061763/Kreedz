package com.sd.android.kreedz.feature.ranking.screen.country

import android.os.Bundle
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.COUNTRY_RANKING)
internal class CountryRankingActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setPageContent {
      CountryRankingScreen(
        onClickBack = { finish() },
      )
    }
  }
}