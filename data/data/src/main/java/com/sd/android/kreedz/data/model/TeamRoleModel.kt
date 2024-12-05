package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class TeamRoleModel(
  val role: String,
  val roleName: String,
  val users: List<UserWithIconsModel>,
)