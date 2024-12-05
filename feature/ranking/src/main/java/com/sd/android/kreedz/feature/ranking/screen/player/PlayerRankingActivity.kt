package com.sd.android.kreedz.feature.ranking.screen.player

import androidx.compose.runtime.Composable
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.PLAYER_RANKING)
internal class PlayerRankingActivity : BaseActivity() {
  @Composable
  override fun ContentImpl() {
    PlayerRankingScreen(
      onClickBack = {
        finish()
      }
    )
  }
}