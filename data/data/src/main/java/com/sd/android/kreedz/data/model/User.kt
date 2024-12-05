package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class UserModel(
  val id: String,
  val nickname: String,
  val country: String?,
)

@Immutable
data class UserWithIconsModel(
  val id: String,
  val nickname: String,
  val country: String?,
  val icons: UserIconsModel,
)

@Immutable
data class UserIconsModel(
  val isVip: Boolean,
  val isRecordHolder: Boolean,
  val isLJRecordHolder: Boolean,
  val isTournamentRank1: Boolean,
  val isTournamentRank2: Boolean,
  val isTournamentRank3: Boolean,
  val isMapper: Boolean,
  val isMovieEditor: Boolean,
) {
  companion object {
    val Default = UserIconsModel(
      isVip = false,
      isRecordHolder = false,
      isLJRecordHolder = false,
      isTournamentRank1 = false,
      isTournamentRank2 = false,
      isTournamentRank3 = false,
      isMapper = false,
      isMovieEditor = false,
    )
  }
}