package com.sd.android.kreedz.data.network.model

data class NetLatestBlog(
  val lastArticles: List<NetBlog>,
)

data class NetBlog(
  val id: String,
  val title: String,
  val articleDate: String,

  val author: String,
  val authorId: String,
  val authorCountry: String?,
  val icons: NetIcons,

  val htmlContent: String = "",
)