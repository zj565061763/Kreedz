package com.sd.android.kreedz.exportImpl

import com.didi.drouter.annotation.Service
import com.sd.android.kreedz.BuildConfig
import com.sd.android.kreedz.data.network.export.FSHttpUrl

@Service(function = [FSHttpUrl::class])
class FSHttpUrlImpl : FSHttpUrl {
  override fun getServerUrl(): String = "https://${BuildConfig.HOST}/"
  override fun getApiUrl(): String = "${getServerUrl()}api/"
  override fun getOnlineUsersWSUrl(): String = "wss://${BuildConfig.HOST}/api/ws/users"
  override fun getRefreshTokenUrl(): String = "https://${BuildConfig.HOST}/api/auth/refresh-token"
}