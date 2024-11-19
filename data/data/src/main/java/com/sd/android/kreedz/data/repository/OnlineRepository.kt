package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.local.LocalUserAccountModel
import com.sd.android.kreedz.data.mapper.asUserIconsModel
import com.sd.android.kreedz.data.model.OnlineUsersModel
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.data.network.NetOnlineUsersDataSource
import com.sd.android.kreedz.data.network.model.NetOnlineUser
import com.sd.lib.coroutines.fGlobalLaunch
import com.sd.lib.xlog.FLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

fun OnlineRepository(): OnlineRepository = OnlineRepositoryImpl

interface OnlineRepository {
   fun getOnlineUsersFlow(): Flow<OnlineUsersModel>
   suspend fun sync()
}

private object OnlineRepositoryImpl : OnlineRepository, FLogger {

   override fun getOnlineUsersFlow(): Flow<OnlineUsersModel> {
      return NetOnlineUsersDataSource.onlineUsersFlow
         .map { data ->
            OnlineUsersModel(
               guestsCount = data?.userCount ?: 0,
               users = data?.users?.map(NetOnlineUser::asUserWithIconsModel) ?: emptyList()
            )
         }.flowOn(Dispatchers.IO)
   }

   override suspend fun sync() {
      // Do nothing
   }

   init {
      fGlobalLaunch {
         LocalUserAccountModel.userIdFlow.collect { userId ->
            NetOnlineUsersDataSource.setUserId(userId ?: "")
         }
      }
   }
}

private fun NetOnlineUser.asUserWithIconsModel(): UserWithIconsModel {
   return UserWithIconsModel(
      id = id,
      nickname = pseudo,
      country = country,
      icons = icons?.asUserIconsModel() ?: UserIconsModel.Default,
   )
}