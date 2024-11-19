package com.sd.android.kreedz.screen.user

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.UserAchievementModel
import com.sd.android.kreedz.data.model.UserProfileModel
import com.sd.android.kreedz.data.model.UserRecentActivityModel
import com.sd.android.kreedz.data.model.UserRecentRecordModel
import com.sd.android.kreedz.data.model.UserRecordStatsModel
import com.sd.android.kreedz.data.repository.UserRepository
import com.sd.lib.coroutines.FLoader
import com.sd.lib.retry.ktx.fNetRetry
import kotlinx.coroutines.delay

class UserVM : BaseViewModel<UserVM.State, Any>(State()) {
   private val _repository = UserRepository()
   private val _loader = FLoader()
   private val _recordsStatsLoader = FLoader()

   fun load(userId: String) {
      vmLaunch {
         _loader.load {
            updateState { it.copy(userId = userId) }
            loadData()
         }
      }
   }

   fun retry() {
      vmLaunch {
         _loader.load {
            delay(500)
            loadData()
         }
      }
   }

   private suspend fun loadData() {
      val userId = state.userId
      loadRecordsStats(userId)

      val data = _repository.getUserProfile(userId)
      bindData(data)
   }

   private fun loadRecordsStats(userId: String) {
      vmLaunch {
         _recordsStatsLoader.load {
            fNetRetry {
               _repository.getUserRecordsStats(userId)
            }.getOrThrow()
         }.onSuccess { data ->
            updateState {
               it.copy(recordsStats = data)
            }
         }
      }
   }

   private fun bindData(data: UserProfileModel) {
      updateState {
         it.copy(
            nickname = data.nickname,
            country = data.country,
            countryName = data.countryName,
            lastVisit = data.lastVisit,
            regDate = data.regDateStr,
            roles = data.roles,
            siteVisits = data.siteVisits,
            chatBoxComments = data.chatBoxComments,
            recentActivity = data.recentActivity,
            recentRecords = data.recentRecords,
            achievements = data.achievements,
         )
      }
   }

   init {
      vmLaunch {
         _loader.stateFlow.collect { data ->
            updateState {
               it.copy(
                  isLoading = data.isLoading,
                  result = data.result,
               )
            }
         }
      }

      vmLaunch {
         _recordsStatsLoader.loadingFlow.collect { data ->
            updateState { state ->
               state.copy(
                  isLoadingRecordsStats = data,
                  recordsStats = if (data) {
                     state.recordsStats.copy(
                        rank = "-",
                        numberCurrentRecords = "-",
                        numberTotalRecords = "-",
                        numberDifferentMaps = "-",
                     )
                  } else state.recordsStats
               )
            }
         }
      }
   }

   @Immutable
   data class State(
      val userId: String = "",

      val isLoading: Boolean = false,
      val result: Result<Unit>? = null,

      val isLoadingRecordsStats: Boolean = false,

      val nickname: String = "",
      val country: String? = null,
      val countryName: String? = null,
      val lastVisit: String = "",
      val regDate: String = "",
      val roles: List<String> = emptyList(),
      val siteVisits: String = "",
      val chatBoxComments: String = "",

      val recentActivity: List<UserRecentActivityModel> = emptyList(),
      val recentRecords: List<UserRecentRecordModel> = emptyList(),
      val achievements: List<UserAchievementModel> = emptyList(),

      val recordsStats: UserRecordStatsModel = UserRecordStatsModel(
         rank = "-",
         numberCurrentRecords = "-",
         numberTotalRecords = "-",
         numberDifferentMaps = "-",
         firstRecord = null,
         lastRecord = null,
         currentRecords = null,
         previousRecords = null,
      ),
   )
}