package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class UserProfileModel(
  val nickname: String,
  val previousNickname: List<String>,
  val country: String?,
  val countryName: String?,
  val regDateStr: String,
  val lastVisit: String,
  val siteVisits: String,
  val chatBoxComments: String,
  val roles: List<String>,
  val recentActivity: List<UserRecentActivityModel>,
  val recentRecords: List<UserRecentRecordModel>,
  val achievements: List<UserAchievementModel>,
)

@Immutable
data class UserRecentActivityModel(
  val activityDateStr: String,
  val isChatBoxComment: Boolean,
  val isNewsComment: Boolean,
  val newsId: String?,
  val newsName: String?,
)

@Immutable
data class UserRecentRecordModel(
  val mapName: String,
  val timeStr: String,
  val currentRecord: Boolean,
  val dateStr: String?,
  val mapId: String?,
  val newsId: String?,
  val newsName: String?,
)

@Immutable
data class UserAchievementModel(
  val title: String,
  val rank: Int?,
  val dateStr: String?,
  val newsId: String?,
)