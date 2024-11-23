package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.mapper.asUserIconsModel
import com.sd.android.kreedz.data.model.NewsCommentGroupModel
import com.sd.android.kreedz.data.model.NewsCommentListModel
import com.sd.android.kreedz.data.model.NewsCommentModel
import com.sd.android.kreedz.data.model.NewsCommentReplyModel
import com.sd.android.kreedz.data.model.NewsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetNews
import com.sd.android.kreedz.data.network.model.NetNewsComment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun NewsRepository(): NewsRepository = NewsRepositoryImpl()

interface NewsRepository {
   suspend fun getLatest(page: Int): List<NewsModel>
   suspend fun getNews(newsId: String): NewsModel
   suspend fun comments(newsId: String): NewsCommentListModel
   suspend fun sendComment(newsId: String, content: String, replyCommentId: String?)
   suspend fun deleteComment(id: String)
}

private class NewsRepositoryImpl : NewsRepository {
   private val _netDataSource = NetDataSource()

   override suspend fun getLatest(page: Int): List<NewsModel> {
      val data = _netDataSource.getLatestNews(page)
      return withContext(Dispatchers.IO) {
         data.lastNews.map { it.asNewsModel() }
      }
   }

   override suspend fun getNews(newsId: String): NewsModel {
      val data = _netDataSource.getNews(newsId)
      return data.asNewsModel()
   }

   override suspend fun comments(newsId: String): NewsCommentListModel {
      val data = _netDataSource.newsComments(newsId)
      val groups = withContext(Dispatchers.IO) {
         val (parents, children) = data.partition { it.parentId.isNullOrBlank() }
         val group = children.groupBy { it.parentId }

         fun findAllChildren(comment: NetNewsComment): List<NetNewsComment> {
            val list = group[comment.id] ?: emptyList()
            list.forEach { it.parent = comment }
            return list + (list.flatMap { findAllChildren(it) })
         }

         parents.map { parent ->
            val netChildren = findAllChildren(parent)
            NewsCommentGroupModel(
               comment = parent.asNewsCommentModel(),
               children = netChildren.asNewsCommentReplyModels(),
            )
         }
      }
      return NewsCommentListModel(
         count = data.size,
         groups = groups,
      )
   }

   override suspend fun sendComment(
      newsId: String,
      content: String,
      replyCommentId: String?,
   ) {
      _netDataSource.newsSendComment(
         newsId = newsId,
         content = content,
         replyCommentId = replyCommentId,
      )
   }

   override suspend fun deleteComment(id: String) {
      _netDataSource.newsDeleteComment(id)
   }
}

private fun NetNews.asNewsModel(): NewsModel {
   return NewsModel(
      id = id,
      title = title,
      htmlContent = htmlContent,
      dateStr = newsDate,
      author = UserWithIconsModel(
         id = authorId,
         nickname = author,
         country = authorCountry,
         icons = icons.asUserIconsModel(),
      ),
   )
}

private fun NetNewsComment.asNewsCommentModel(): NewsCommentModel {
   return NewsCommentModel(
      id = id,
      comment = message,
      dateStr = commentDate,
      author = UserWithIconsModel(
         id = authorId ?: "",
         nickname = author,
         country = country,
         icons = icons.asUserIconsModel(),
      ),
   )
}

private fun List<NetNewsComment>.asNewsCommentReplyModels(): List<NewsCommentReplyModel> {
   return map { item ->
      NewsCommentReplyModel(
         comment = item.asNewsCommentModel(),
         reply = item.parent?.asNewsCommentReplyModel(),
      )
   }
}

private fun NetNewsComment.asNewsCommentReplyModel(): NewsCommentReplyModel {
   return NewsCommentReplyModel(
      comment = asNewsCommentModel(),
      reply = null,
   )
}