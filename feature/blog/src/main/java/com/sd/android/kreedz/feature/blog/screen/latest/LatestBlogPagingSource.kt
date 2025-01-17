package com.sd.android.kreedz.feature.blog.screen.latest

import com.sd.android.kreedz.data.model.NewsModel
import com.sd.android.kreedz.data.repository.BlogRepository
import com.sd.lib.kmp.paging.KeyIntPagingSource
import com.sd.lib.kmp.paging.LoadParams
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.ld

internal class LatestBlogPagingSource : KeyIntPagingSource<NewsModel>(), FLogger {
  private val _repository = BlogRepository()

  override suspend fun loadImpl(params: LoadParams<Int>): List<NewsModel> {
    ld { "load $params" }
    return _repository.getLatest(params.key)
  }
}