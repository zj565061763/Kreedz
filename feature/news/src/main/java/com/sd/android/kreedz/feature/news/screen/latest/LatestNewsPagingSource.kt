package com.sd.android.kreedz.feature.news.screen.latest

import com.sd.android.kreedz.data.model.NewsModel
import com.sd.android.kreedz.data.repository.NewsRepository
import com.sd.lib.paging.KeyIntPagingSource
import com.sd.lib.paging.LoadParams
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.ld

internal class LatestNewsPagingSource : KeyIntPagingSource<NewsModel>(), FLogger {
  private val _repository = NewsRepository()

  override suspend fun loadImpl(params: LoadParams<Int>): List<NewsModel> {
    ld { "load $params" }
    return _repository.getLatest(params.key)
  }
}