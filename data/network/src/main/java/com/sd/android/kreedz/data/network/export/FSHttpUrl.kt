package com.sd.android.kreedz.data.network.export

import com.didi.drouter.api.DRouter

interface FSHttpUrl {
  fun getServerUrl(): String
  fun getApiUrl(): String
  fun getOnlineUsersWSUrl(): String
  fun getRefreshTokenUrl(): String
}

val fsHttpUrl: FSHttpUrl
  get() = DRouter.build(FSHttpUrl::class.java).getService()