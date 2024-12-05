package com.sd.android.kreedz.feature.news.screen.latest

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.lib.compose.paging.fPagerFlow

class LatestNewsVM : BaseViewModel<Unit, Unit>(Unit) {
  val itemsFlow = fPagerFlow(prefetchDistance = 5) {
    LatestNewsPagingSource()
  }.cachedIn(viewModelScope)
}