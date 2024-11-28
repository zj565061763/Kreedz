package com.sd.android.kreedz.feature.blog.screen.latest

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.lib.compose.paging.fPagerFlow

class LatestBlogVM : BaseViewModel<Unit, Unit>(Unit) {
   val itemsFlow = fPagerFlow(prefetchDistance = 5) {
      LatestBlogPagingSource()
   }.cachedIn(viewModelScope)
}