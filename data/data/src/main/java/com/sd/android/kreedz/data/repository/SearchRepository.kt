package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.model.SearchNewsModel
import com.sd.android.kreedz.data.model.SearchResultModel
import com.sd.android.kreedz.data.model.SearchUserModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetSearchNews
import com.sd.android.kreedz.data.network.model.NetSearchUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun SearchRepository(): SearchRepository = SearchRepositoryImpl()

interface SearchRepository {
  suspend fun search(keyword: String): SearchResultModel
}

private class SearchRepositoryImpl : SearchRepository {
  private val _netDataSource = NetDataSource()

  override suspend fun search(keyword: String): SearchResultModel {
    val data = _netDataSource.search(keyword)
    return withContext(Dispatchers.IO) {
      SearchResultModel(
        listUser = data.userSearch.map { it.asSearchUserModel() },
        listNews = data.newsSearch.map { it.asSearchNewsModel() },
      )
    }
  }
}

private fun NetSearchUser.asSearchUserModel(): SearchUserModel {
  return SearchUserModel(
    id = userId,
    nickname = nickname,
    country = country,
  )
}

private fun NetSearchNews.asSearchNewsModel(): SearchNewsModel {
  return SearchNewsModel(
    id = id,
    title = title,
    dateStr = date,
    author = SearchUserModel(
      id = userId,
      nickname = nickname,
      country = country,
    ),
    extract = extract.removeMark(),
  )
}

private fun String.removeMark(): String {
  val input = this
  return "<mark>(.*?)</mark>"
    .toRegex()
    .replace(input) { result ->
      result.groups[1]?.value ?: ""
    }
}