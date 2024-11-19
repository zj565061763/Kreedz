package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.database.entity.UserEntity
import com.sd.android.kreedz.data.model.UserAchievementModel
import com.sd.android.kreedz.data.model.UserProfileModel
import com.sd.android.kreedz.data.model.UserRecentActivityModel
import com.sd.android.kreedz.data.model.UserRecentRecordModel
import com.sd.android.kreedz.data.model.UserRecordModel
import com.sd.android.kreedz.data.model.UserRecordStatsModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetUserAchievement
import com.sd.android.kreedz.data.network.model.NetUserProfile
import com.sd.android.kreedz.data.network.model.NetUserRecentActivity
import com.sd.android.kreedz.data.network.model.NetUserRecentRecord
import com.sd.android.kreedz.data.network.model.NetUserRecord
import com.sd.android.kreedz.data.network.model.NetUserRecordStats
import com.sd.android.kreedz.data.repository.dao.DaoUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun UserRepository(): UserRepository = UserRepositoryImpl()

interface UserRepository {
   suspend fun getUserProfile(id: String): UserProfileModel
   suspend fun getUserRecordsStats(id: String): UserRecordStatsModel
}

private class UserRepositoryImpl : UserRepository {
   private val _netDataSource = NetDataSource()
   private val _daoUser = DaoUserRepository()

   override suspend fun getUserProfile(id: String): UserProfileModel {
      require(id.isNotBlank()) { "User id is blank" }
      val data = _netDataSource.getUserProfile(id)
      _daoUser.insertOrIgnore(
         UserEntity(
            id = data.id.takeUnless { it.isBlank() } ?: id,
            nickname = data.pseudo,
            country = data.countryCode,
         )
      )
      return withContext(Dispatchers.IO) {
         data.asUserProfileModel()
      }
   }

   override suspend fun getUserRecordsStats(id: String): UserRecordStatsModel {
      require(id.isNotBlank()) { "User id is blank" }
      val data = _netDataSource.getUserRecordsStats(id)
      return withContext(Dispatchers.IO) {
         data.asUserRecordStatsModel()
      }
   }
}

private fun NetUserProfile.asUserProfileModel(): UserProfileModel {
   return UserProfileModel(
      nickname = pseudo,
      previousNickname = previousPseudo ?: emptyList(),
      country = countryCode,
      countryName = countryName,
      regDateStr = registrationDate ?: "",
      lastVisit = lastVisit ?: "",
      siteVisits = visitsSince ?: "",
      chatBoxComments = chatboxComments ?: "",
      roles = roles ?: emptyList(),
      recentActivity = recentActivity?.map { it.asUserRecentActivityModel() } ?: emptyList(),
      recentRecords = recentRecords?.map { it.asUserRecentRecordModel() } ?: emptyList(),
      achievements = achievements?.map { it.asUserAchievementModel() } ?: emptyList(),
   )
}

private fun NetUserRecentActivity.asUserRecentActivityModel(): UserRecentActivityModel {
   return UserRecentActivityModel(
      activityDateStr = activityDate,
      isChatBoxComment = isChatBoxComment,
      isNewsComment = isComment,
      newsId = newsId,
      newsName = commentNewsName,
   )
}

private fun NetUserRecentRecord.asUserRecentRecordModel(): UserRecentRecordModel {
   return UserRecentRecordModel(
      mapName = mapName,
      timeStr = time,
      currentRecord = currentRecord,
      dateStr = releaseDate,
      mapId = mapId,
      newsId = newsId,
      newsName = newsName,
   )
}

private fun NetUserAchievement.asUserAchievementModel(): UserAchievementModel {
   return UserAchievementModel(
      title = description,
      rank = rank,
      dateStr = achievementDate,
      newsId = newsId,
   )
}

private fun NetUserRecordStats.asUserRecordStatsModel(): UserRecordStatsModel {
   return UserRecordStatsModel(
      rank = rank,
      numberCurrentRecords = numberCurrentRecords,
      numberTotalRecords = numberTotalRecords,
      numberDifferentMaps = numberDifferentMaps,
      firstRecord = firstRecord?.asUserRecordModel(),
      lastRecord = lastRecord?.asUserRecordModel(),
      currentRecords = currentRecords?.map { it.asUserRecordModel() },
      previousRecords = previousRecords?.map { it.asUserRecordModel() },
   )
}

private fun NetUserRecord.asUserRecordModel(): UserRecordModel {
   return UserRecordModel(
      mapName = mapName,
      timeStr = time,
      dateStr = date,
      mapId = mapId,
      timeDifferenceStr = timeDifference,
   )
}