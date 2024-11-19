package com.sd.android.kreedz.screen.blog

import com.sd.android.kreedz.data.model.BlogModel
import com.sd.android.kreedz.data.repository.BlogRepository
import com.sd.lib.compose.paging.FIntPagingSource

class BlogPagingSource : FIntPagingSource<BlogModel>(
   initialKey = 0,
) {
   private val _repository = BlogRepository()

   override suspend fun loadImpl(params: LoadParams<Int>, key: Int): List<BlogModel> {
      return _repository.getLatestBlog(key)
   }
}