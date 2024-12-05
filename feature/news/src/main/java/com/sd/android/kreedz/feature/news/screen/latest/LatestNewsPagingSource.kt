package com.sd.android.kreedz.feature.news.screen.latest

import com.sd.android.kreedz.data.model.NewsModel
import com.sd.android.kreedz.data.repository.NewsRepository
import com.sd.lib.compose.paging.FIntPagingSource

internal class LatestNewsPagingSource : FIntPagingSource<NewsModel>(
  initialKey = 0,
) {
  private val _repository = NewsRepository()

  override suspend fun loadImpl(params: LoadParams<Int>, key: Int): List<NewsModel> {
    return _repository.getLatest(key)
  }
}