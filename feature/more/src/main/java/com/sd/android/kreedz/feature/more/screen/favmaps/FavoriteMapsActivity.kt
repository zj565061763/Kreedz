package com.sd.android.kreedz.feature.more.screen.favmaps

import androidx.compose.runtime.Composable
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.FAVORITE_MAPS)
internal class FavoriteMapsActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      FavoriteMapsScreen(
         onClickBack = {
            finish()
         }
      )
   }
}