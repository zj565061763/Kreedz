package com.sd.android.kreedz.data.model

data class OnlineUsersModel(
  val guestsCount: Int,
  val users: List<UserWithIconsModel>,
)