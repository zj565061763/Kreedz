package com.sd.android.kreedz.feature.blog.screen.blog

import com.sd.android.kreedz.data.repository.BlogRepository
import com.sd.android.kreedz.data.repository.NewsRepository
import com.sd.android.kreedz.feature.news.screen.news.NewsVM

internal class BlogVM : NewsVM() {
  override fun newsRepository(): NewsRepository {
    return BlogRepository()
  }
}