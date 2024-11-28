package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetBlog
import com.sd.android.kreedz.data.network.model.NetLatestNews
import com.sd.android.kreedz.data.network.model.NetNews
import com.sd.android.kreedz.data.network.model.NetNewsComment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun BlogRepository(): NewsRepository = NewsRepository(dataSource = BlogDataSourceImpl())

private class BlogDataSourceImpl : NewsDataSource {
   private val _netDataSource = NetDataSource()

   override suspend fun getLatest(page: Int): NetLatestNews {
      val data = _netDataSource.getLatestBlog(page)
      return withContext(Dispatchers.IO) {
         NetLatestNews(
            lastNews = data.lastArticles.map { it.asNetNews() }
         )
      }
   }

   override suspend fun getNews(id: String): NetNews {
      return _netDataSource.getBlog(id).asNetNews()
   }

   override suspend fun comments(id: String): List<NetNewsComment> {
      return _netDataSource.blogComments(id)
   }

   override suspend fun sendComment(id: String, content: String, replyCommentId: String?) {
      _netDataSource.blogSendComment(
         blogId = id,
         content = content,
         replyCommentId = replyCommentId,
      )
   }

   override suspend fun deleteComment(commentId: String) {
      _netDataSource.blogDeleteComment(commentId)
   }
}

private fun NetBlog.asNetNews(): NetNews {
   return NetNews(
      id = id,
      title = title,
      newsDate = articleDate,
      author = author,
      authorId = authorId,
      authorCountry = authorCountry,
      icons = icons,
      htmlContent = htmlContent,
   )
}