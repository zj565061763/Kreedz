package com.sd.android.kreedz.data.network.export

interface FSHttpUrl {
   fun getServerUrl(): String
   fun getApiUrl(): String
   fun getOnlineUsersWSUrl(): String
   fun getRefreshTokenUrl(): String
}