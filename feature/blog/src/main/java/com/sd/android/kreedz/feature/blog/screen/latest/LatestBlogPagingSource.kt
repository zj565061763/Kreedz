package com.sd.android.kreedz.feature.blog.screen.latest

import com.sd.android.kreedz.data.model.NewsModel
import com.sd.android.kreedz.data.repository.BlogRepository
import com.sd.lib.compose.paging.FIntPagingSource

internal class LatestBlogPagingSource : FIntPagingSource<NewsModel>(
  initialKey = 0,
) {
  private val _repository = BlogRepository()

  override suspend fun loadImpl(params: LoadParams<Int>, key: Int): List<NewsModel> {
    return _repository.getLatest(key)
  }
}