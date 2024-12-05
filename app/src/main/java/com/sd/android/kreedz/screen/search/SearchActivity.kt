package com.sd.android.kreedz.screen.search

import androidx.compose.runtime.Composable
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter

@Router(path = AppRouter.SEARCH)
class SearchActivity : BaseActivity() {
  @Composable
  override fun ContentImpl() {
    SearchScreen(
      onClickBack = {
        finish()
      },
    )
  }
}