package com.sd.android.kreedz.feature.ranking.screen.country

import androidx.compose.runtime.Composable
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.COUNTRY_RANKING)
internal class CountryRankingActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      CountryRankingScreen(
         onClickBack = {
            finish()
         }
      )
   }
}