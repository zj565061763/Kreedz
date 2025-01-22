package com.sd.android.kreedz.feature.ranking.screen.player

import android.os.Bundle
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.PLAYER_RANKING)
internal class PlayerRankingActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setPageContent {
      PlayerRankingScreen(
        onClickBack = { finish() },
      )
    }
  }
}