package com.sd.android.kreedz.data.network

import android.content.Context
import com.sd.android.kreedz.data.network.export.fsHttpUrl
import com.sd.android.kreedz.data.network.http.AppCookieJar
import com.sd.android.kreedz.data.network.http.moshi.RecordDateAdapter
import com.sd.android.kreedz.data.network.http.moshi.RecordTimeAdapter
import com.sd.android.kreedz.data.network.http.newOkHttpClient
import com.sd.android.kreedz.data.network.http.newRetrofit
import com.sd.lib.moshi.fMoshi
import com.sd.lib.xlog.FLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Retrofit

object ModuleNetwork : FLogger {
   private lateinit var _retrofit: Retrofit
   private lateinit var _client: OkHttpClient
   private lateinit var _cookieJar: AppCookieJar

   fun init(
      context: Context,
      isRelease: Boolean,
   ) {
      if (ModuleNetwork::_retrofit.isInitialized) return
      _cookieJar = AppCookieJar(context)
      _client = newOkHttpClient(isRelease, _cookieJar)
      _retrofit = newRetrofit(
         baseUrl = fsHttpUrl.getApiUrl(),
         client = _client,
         moshi = fMoshi.newBuilder()
            .add(RecordTimeAdapter())
            .add(RecordDateAdapter())
            .build(),
      )
   }

   suspend fun hasToken(): Boolean {
      return withContext(Dispatchers.IO) {
         _cookieJar.hasToken(
            fsHttpUrl.getServerUrl().toHttpUrl()
         )
      }
   }

   fun connectOnlineUsersWebSocket(userId: String, listener: WebSocketListener): WebSocket {
      val url = fsHttpUrl.getOnlineUsersWSUrl().let {
         if (userId.isBlank()) {
            it
         } else {
            "${it}?id=$userId"
         }
      }
      val request = Request.Builder()
         .url(url)
         .build()
      return _client.newWebSocket(request, listener)
   }

   internal fun <T> createApi(clazz: Class<T>): T {
      return _retrofit.create(clazz)
   }
}