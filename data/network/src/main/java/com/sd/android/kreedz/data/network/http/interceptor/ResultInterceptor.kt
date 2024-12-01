package com.sd.android.kreedz.data.network.http.interceptor

import com.sd.android.kreedz.data.network.exception.HttpMessageException
import com.sd.android.kreedz.data.network.exception.HttpTooManyRequestsException
import com.sd.android.kreedz.data.network.http.AppApi
import com.sd.android.kreedz.data.network.model.NetFailureResponse
import com.sd.lib.moshi.fMoshi
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

internal class ResultInterceptor : AppApiInterceptor() {
   private val _failureResponseAdapter = fMoshi.adapter(NetFailureResponse::class.java)

   override fun interceptImpl(chain: Interceptor.Chain, annotation: AppApi): Response {
      val request = chain.request()

      val response = chain.proceed(request)
      if (!response.isSuccessful) {
         when (response.code) {
            429 -> throw HttpTooManyRequestsException()
            else -> return response.handleFailureResponse()
         }
      }

      val body = response.body
         ?: return response.newBuilder()
            .body("".toResponseBody())
            .build()

      return if (annotation.resultLog) {
         val bodyString = body.string()
         request.logDebug { "result：$bodyString" }
         response.newBuilder()
            .body(bodyString.toResponseBody(body.contentType()))
            .build()
      } else {
         response
      }
   }

   private fun Response.handleFailureResponse(): Response {
      val failureResponse = runCatching {
         val json = body?.string() ?: ""
         checkNotNull(_failureResponseAdapter.fromJson(json))
      }.getOrNull() ?: return this

      val message = failureResponse.message
      if (message.isBlank()) return this

      throw HttpMessageException(message)
   }
}