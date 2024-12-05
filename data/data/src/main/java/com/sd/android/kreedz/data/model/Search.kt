package com.sd.android.kreedz.data.model

data class SearchResultModel(
  val listUser: List<SearchUserModel>,
  val listNews: List<SearchNewsModel>,
)

data class SearchUserModel(
  val id: String?,
  val nickname: String?,
  val country: String?,
)

data class SearchNewsModel(
  val id: String,
  val title: String,
  val dateStr: String,
  val author: SearchUserModel,
  val extract: String,
)