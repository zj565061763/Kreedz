package com.sd.android.kreedz.data.network.model

data class NetLatestNews(
  val lastNews: List<NetNews>,
)

data class NetNews(
  val id: String,
  val title: String,
  val newsDate: String,

  val author: String,
  val authorId: String,
  val authorCountry: String?,
  val icons: NetIcons,

  val htmlContent: String = "",
)

data class NetNewsComment(
  val id: String,
  val message: String,
  val commentDate: String,
  val author: String,
  val country: String?,
  val icons: NetIcons,
  val authorId: String?,
  val parentId: String?,
) {
  var parent: NetNewsComment? = null
}