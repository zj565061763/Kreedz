package com.sd.android.kreedz.feature.blog.screen.latest

import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.NewsModel
import com.sd.lib.paging.FPaging
import com.sd.lib.paging.Paging

class LatestBlogVM : BaseViewModel<Unit, Unit>(Unit) {
  val paging: Paging<NewsModel> = FPaging(
    refreshKey = 0,
    pagingSource = LatestBlogPagingSource(),
  )
}