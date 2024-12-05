package com.sd.android.kreedz.data.network.http

import com.sd.android.kreedz.data.network.http.interceptor.AuthInterceptor
import com.sd.android.kreedz.data.network.http.interceptor.FirstInterceptor
import com.sd.android.kreedz.data.network.http.interceptor.FormBodyTransformInterceptor
import com.sd.android.kreedz.data.network.http.interceptor.ResultInterceptor
import com.squareup.moshi.Moshi
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

internal fun newRetrofit(
  baseUrl: String,
  client: OkHttpClient,
  moshi: Moshi,
): Retrofit {
  require(baseUrl.isNotBlank()) { "baseUrl is blank" }
  return Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(client)
    .build()
}

internal fun newOkHttpClient(
  isRelease: Boolean,
  cookieJar: CookieJar,
): OkHttpClient {
  val trustManager = object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, type: String?) = Unit
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, type: String?) = Unit
    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
  }

  val sslContext = SSLContext.getInstance("TLS").apply {
    init(null, arrayOf(trustManager), null)
  }

  val timeout = 10_000L

  return OkHttpClient.Builder()
    .addInterceptor(FirstInterceptor())
    .addInterceptor(FormBodyTransformInterceptor())
    .addInterceptor(ResultInterceptor())
    .addInterceptor(AuthInterceptor())
    .let {
      if (isRelease) it else {
        it.addInterceptor(
          HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.HEADERS
          }
        )
      }
    }
    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
    .writeTimeout(timeout, TimeUnit.MILLISECONDS)
    .readTimeout(timeout, TimeUnit.MILLISECONDS)
    .pingInterval(10, TimeUnit.SECONDS)
    .sslSocketFactory(sslContext.socketFactory, trustManager)
    .hostnameVerifier { _, _ -> true }
    .cookieJar(cookieJar)
    .build()
}