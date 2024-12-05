package com.sd.android.kreedz.data.network.http.interceptor

import com.sd.android.kreedz.data.network.http.AppApi
import com.sd.lib.moshi.fMoshi
import com.squareup.moshi.Types
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

/**
 * FormBody -> json body
 */
internal class FormBodyTransformInterceptor : AppApiInterceptor() {
  private val _moshiAdapter = fMoshi.adapter<Map<String, String>>(
    Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
  )

  override fun interceptImpl(chain: Interceptor.Chain, annotation: AppApi): Response {
    val request = chain.request()
    if (!annotation.transformFormBody) {
      return chain.proceed(request)
    }

    val body = request.body
    if (body !is FormBody) {
      return chain.proceed(request)
    }

    if (body.size == 0) {
      return chain.proceed(request)
    }

    val map = mutableMapOf<String, String>()
    repeat(body.size) { index ->
      val key = body.name(index)
      val value = body.value(index)
      map[key] = value
    }

    val jsonBody = _moshiAdapter.toJson(map)
    val newRequest = request.newBuilder()
      .method(
        method = request.method,
        body = jsonBody.toRequestBody("application/json".toMediaType())
      )
      .build()

    return chain.proceed(newRequest)
  }
}