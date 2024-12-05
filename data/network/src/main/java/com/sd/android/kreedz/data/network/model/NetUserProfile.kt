package com.sd.android.kreedz.data.network.model

data class NetUserProfile(
  val id: String,
  val pseudo: String,
  val previousPseudo: List<String>?,
  val countryCode: String?,
  val countryName: String?,
  val registrationDate: String?,
  val lastVisit: String?,
  val visitsSince: String?,
  val chatboxComments: String?,
  val roles: List<String>?,
  val recentActivity: List<NetUserRecentActivity>?,
  val recentRecords: List<NetUserRecentRecord>?,
  val achievements: List<NetUserAchievement>?,
  val icons: NetIcons,
)

data class NetUserRecentActivity(
  val activityDate: String,
  val isChatBoxComment: Boolean,
  val isComment: Boolean,
  val newsId: String?,
  val commentNewsName: String?,
)

data class NetUserRecentRecord(
  val mapName: String,
  val time: String,
  val currentRecord: Boolean,
  val releaseDate: String?,
  val newsName: String?,
  val mapId: String?,
  val newsId: String?,
)

data class NetUserAchievement(
  val description: String,
  val rank: Int?,
  val achievementDate: String?,
  val newsId: String?,
)