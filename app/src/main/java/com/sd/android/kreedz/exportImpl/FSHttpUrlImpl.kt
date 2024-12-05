package com.sd.android.kreedz.exportImpl

import com.didi.drouter.annotation.Service
import com.sd.android.kreedz.data.network.export.FSHttpUrl

@Service(function = [FSHttpUrl::class])
class FSHttpUrlImpl : FSHttpUrl {
  override fun getServerUrl(): String = "https://xtreme-jumps.eu/"
  override fun getApiUrl(): String = "${getServerUrl()}api/"
  override fun getOnlineUsersWSUrl(): String = "wss://xtreme-jumps.eu/api/ws/users"
  override fun getRefreshTokenUrl(): String = "https://xtreme-jumps.eu/api/auth/refresh-token"
}