package com.sd.android.kreedz.data.network.model

data class NetSearch(
   val userSearch: List<NetSearchUser>,
   val newsSearch: List<NetSearchNews>,
)

data class NetSearchUser(
   val userId: String?,
   val nickname: String?,
   val country: String?,
)

data class NetSearchNews(
   val id: String,
   val title: String,
   val date: String,
   val extract: String,
   val userId: String?,
   val nickname: String?,
   val country: String?,
)