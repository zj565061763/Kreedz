package com.sd.android.kreedz.data.network.model

data class NetChatBoxMessage(
  val id: String,
  val message: String,
  val commentDate: String,
  val author: String,
  val country: String?,
  val icons: NetIcons,
  val authorId: String?,
)