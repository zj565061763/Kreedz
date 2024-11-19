package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.local.LocalAppConfig
import com.sd.android.kreedz.data.local.LocalUserAccountModel
import com.sd.android.kreedz.data.mapper.asUserIconsModel
import com.sd.android.kreedz.data.model.UserAccountModel
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.network.ModuleNetwork
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.event.EHttpUnauthorized
import com.sd.android.kreedz.data.network.model.NetLogin
import com.sd.android.kreedz.data.network.model.NetUserProfile
import com.sd.lib.coroutines.FSyncable
import com.sd.lib.coroutines.awaitIdle
import com.sd.lib.coroutines.fGlobalLaunch
import com.sd.lib.coroutines.syncOrThrow
import com.sd.lib.event.FEvent
import com.sd.lib.retry.ktx.fNetRetry
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.li
import com.sd.lib.xlog.lw
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

fun AccountRepository(): AccountRepository = AccountRepositoryImpl

interface AccountRepository {
   fun getUserAccountFlow(): Flow<UserAccountModel?>

   suspend fun login(username: String, password: String)
   suspend fun logout()
   suspend fun syncAccount()

   suspend fun recoverPassword(email: String)
   suspend fun recoverUsername(email: String)

   suspend fun hasLogin(): Boolean
   suspend fun getUserAccount(): UserAccountModel?
   suspend fun getLastLoginUsername(): String
}

private object AccountRepositoryImpl : AccountRepository, FLogger {
   private val _netDataSource = NetDataSource()

   override fun getUserAccountFlow(): Flow<UserAccountModel?> {
      return LocalUserAccountModel.flow
         .map { it?.asUserAccountModel() }
   }

   override suspend fun login(username: String, password: String) {
      _logoutSyncable.awaitIdle()
      val data = _netDataSource.login(username = username, password = password)
      LocalUserAccountModel.replace(data.asLocalUserAccountModel())
      LocalAppConfig.setLastLoginUsername(username)
   }

   override suspend fun logout() {
      // Do not throw sync exception.
      _logoutSyncable.sync()
   }

   override suspend fun syncAccount() {
      _accountSyncable.syncOrThrow()
   }

   override suspend fun recoverPassword(email: String) {
      _netDataSource.recoverPassword(email)
   }

   override suspend fun recoverUsername(email: String) {
      _netDataSource.recoverUsername(email)
   }

   override suspend fun hasLogin(): Boolean {
      return LocalUserAccountModel.get() != null
   }

   override suspend fun getUserAccount(): UserAccountModel? {
      return getUserAccountFlow().firstOrNull()
   }

   override suspend fun getLastLoginUsername(): String {
      return LocalAppConfig.getLastLoginUsername()
   }

   private val _accountSyncable = FSyncable {
      val userId = LocalUserAccountModel.get()?.id
      if (userId.isNullOrBlank()) return@FSyncable
      fNetRetry {
         li { "_accountSyncable $currentCount" }
         _netDataSource.getUserProfile(userId)
      }.onSuccess { data ->
         li { "_accountSyncable success" }
         LocalUserAccountModel.update(data.asLocalUserAccountModel())
      }.onFailure {
         lw { "_accountSyncable failure ${it.stackTraceToString()}" }
      }.getOrThrow()
   }

   private val _logoutSyncable = FSyncable {
      LocalUserAccountModel.replace(null)
      if (ModuleNetwork.hasToken()) {
         fNetRetry {
            li { "_logoutSyncable $currentCount" }
            _netDataSource.logout()
         }.onSuccess {
            li { "_logoutSyncable success" }
         }.onFailure {
            lw { "_logoutSyncable failure ${it.stackTraceToString()}" }
         }.getOrThrow()
      }
   }

   init {
      fGlobalLaunch {
         LocalUserAccountModel.userIdFlow.collectLatest {
            // Do not throw sync exception.
            _accountSyncable.sync()
         }
      }

      fGlobalLaunch {
         FEvent.flowOf<EHttpUnauthorized>().collect {
            li { "receive ${EHttpUnauthorized::class.simpleName}" }
            logout()
         }
      }
   }
}

private fun NetLogin.asLocalUserAccountModel(): LocalUserAccountModel {
   return LocalUserAccountModel(
      id = userId,
      nickname = pseudo,
      country = null,
      countryName = null,
      icons = UserIconsModel.Default,
      roles = emptyList(),
   )
}

private fun NetUserProfile.asLocalUserAccountModel(): LocalUserAccountModel {
   return LocalUserAccountModel(
      id = id,
      nickname = pseudo,
      country = countryCode,
      countryName = countryName,
      icons = icons.asUserIconsModel(),
      roles = roles ?: emptyList(),
   )
}

private fun LocalUserAccountModel.asUserAccountModel(): UserAccountModel {
   return UserAccountModel(
      id = id,
      nickname = nickname,
      country = country,
      countryName = countryName,
      icons = icons,
      roles = roles,
   )
}