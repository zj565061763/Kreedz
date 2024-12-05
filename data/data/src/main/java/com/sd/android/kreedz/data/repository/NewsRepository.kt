package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.mapper.asUserIconsModel
import com.sd.android.kreedz.data.model.NewsCommentGroupModel
import com.sd.android.kreedz.data.model.NewsCommentListModel
import com.sd.android.kreedz.data.model.NewsCommentModel
import com.sd.android.kreedz.data.model.NewsCommentReplyModel
import com.sd.android.kreedz.data.model.NewsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetLatestNews
import com.sd.android.kreedz.data.network.model.NetNews
import com.sd.android.kreedz.data.network.model.NetNewsComment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun NewsRepository(
  dataSource: NewsDataSource = NewsDataSourceImpl(),
): NewsRepository = NewsRepositoryImpl(dataSource = dataSource)

interface NewsRepository {
  suspend fun getLatest(page: Int): List<NewsModel>
  suspend fun getNews(id: String): NewsModel
  suspend fun comments(id: String): NewsCommentListModel
  suspend fun sendComment(id: String, content: String, replyCommentId: String?)
  suspend fun deleteComment(commentId: String)
}

interface NewsDataSource {
  suspend fun getLatest(page: Int): NetLatestNews
  suspend fun getNews(id: String): NetNews
  suspend fun comments(id: String): List<NetNewsComment>
  suspend fun sendComment(id: String, content: String, replyCommentId: String?)
  suspend fun deleteComment(commentId: String)
}

private class NewsRepositoryImpl(
  private val dataSource: NewsDataSource,
) : NewsRepository {

  override suspend fun getLatest(page: Int): List<NewsModel> {
    val data = dataSource.getLatest(page)
    return withContext(Dispatchers.IO) {
      data.lastNews.map { it.asNewsModel() }
    }
  }

  override suspend fun getNews(id: String): NewsModel {
    val data = dataSource.getNews(id)
    return data.asNewsModel()
  }

  override suspend fun comments(id: String): NewsCommentListModel {
    val data = dataSource.comments(id)
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
    id: String,
    content: String,
    replyCommentId: String?,
  ) {
    dataSource.sendComment(
      id = id,
      content = content,
      replyCommentId = replyCommentId,
    )
  }

  override suspend fun deleteComment(commentId: String) {
    dataSource.deleteComment(commentId)
  }
}

private class NewsDataSourceImpl : NewsDataSource {
  private val _netDataSource = NetDataSource()

  override suspend fun getLatest(page: Int): NetLatestNews {
    return _netDataSource.getLatestNews(page)
  }

  override suspend fun getNews(id: String): NetNews {
    return _netDataSource.getNews(id)
  }

  override suspend fun comments(id: String): List<NetNewsComment> {
    return _netDataSource.newsComments(id)
  }

  override suspend fun sendComment(id: String, content: String, replyCommentId: String?) {
    _netDataSource.newsSendComment(
      newsId = id,
      content = content,
      replyCommentId = replyCommentId,
    )
  }

  override suspend fun deleteComment(commentId: String) {
    _netDataSource.newsDeleteComment(commentId)
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
    comment = message.replace("""\r\n""", "\n"),
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