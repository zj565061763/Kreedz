package com.sd.android.kreedz.screen.blog

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.lib.compose.paging.fPagerFlow

class LatestBlogVM : BaseViewModel<Unit, Unit>(Unit) {

   val blogFlow = fPagerFlow(prefetchDistance = 5) {
      BlogPagingSource()
   }.cachedIn(viewModelScope)

}