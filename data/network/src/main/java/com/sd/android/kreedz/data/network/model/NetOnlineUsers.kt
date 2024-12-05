package com.sd.android.kreedz.data.network.model

data class NetOnlineUsers(
  val userCount: Int,
  val users: List<NetOnlineUser>,
)

data class NetOnlineUser(
  val id: String,
  val pseudo: String,
  val country: String?,
  val icons: NetIcons?,
)