package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.mapper.asUserIconsModel
import com.sd.android.kreedz.data.model.BlogModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetBlog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun BlogRepository(): BlogRepository = BlogRepositoryImpl()

interface BlogRepository {
   suspend fun getLatestBlog(page: Int): List<BlogModel>
}

private class BlogRepositoryImpl : BlogRepository {
   private val _netDataSource = NetDataSource()

   override suspend fun getLatestBlog(page: Int): List<BlogModel> {
      val data = _netDataSource.getLatestBlog(page)
      return withContext(Dispatchers.IO) {
         data.lastArticles.map { it.asBlogModel() }
      }
   }
}

private fun NetBlog.asBlogModel(): BlogModel {
   return BlogModel(
      id = id,
      title = title,
      htmlContent = htmlContent,
      dataStr = articleDate,
      author = UserWithIconsModel(
         id = authorId,
         nickname = author,
         country = authorCountry,
         icons = icons.asUserIconsModel(),
      ),
   )
}