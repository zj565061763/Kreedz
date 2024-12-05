package com.sd.android.kreedz.data.network.http

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.li
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

internal class AppCookieJar(
  context: Context,
) : CookieJar, FLogger {

  private val _cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

  fun hasToken(url: HttpUrl): Boolean {
    return _cookieJar.loadForRequest(url).hasToken().second
  }

  override fun loadForRequest(url: HttpUrl): List<Cookie> {
    return _cookieJar.loadForRequest(url)
  }

  override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
    _cookieJar.saveFromResponse(url, cookies).also {
      li { "saveFromResponse $url hasToken:${cookies.hasToken()}" }
    }
  }
}

private fun List<Cookie>.hasToken(): Pair<Boolean, Boolean> {
  val cookie = find { it.name == "refreshToken" }
  if (cookie == null) return (false to false)
  return true to cookie.value.isNotBlank()
}