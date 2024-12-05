package com.sd.android.kreedz.data.network.model

data class NetTeamRole(
  val role: String,
  val users: List<NetTeamUser>,
)

data class NetTeamUser(
  val id: String,
  val pseudo: String,
  val country: String?,
  val icons: NetIcons?,
)