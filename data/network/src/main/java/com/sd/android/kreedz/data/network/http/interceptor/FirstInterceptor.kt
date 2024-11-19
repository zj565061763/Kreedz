package com.sd.android.kreedz.data.network.http.interceptor

import com.sd.android.kreedz.data.network.http.AppApi
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.measureTimedValue

internal class FirstInterceptor : AppApiInterceptor() {

   override fun interceptImpl(chain: Interceptor.Chain, annotation: AppApi): Response {
      val request = chain.request().uuidRequest()
      request.logDebug { "${request.method} ${request.url}" }

      val timedValue = measureTimedValue {
         runCatching {
            chain.proceed(request)
         }
      }

      val result = timedValue.value
      val response = result.getOrNull()

      request.logDebug {
         buildString {
            if (response != null) append("${response.code}|")
            append("time:${timedValue.duration.inWholeMilliseconds}")
         }
      }

      result.onFailure {
         request.logDebug { "onFailure：${it}" }
      }

      return result.getOrThrow()
   }

   companion object {
      private val sUUID = AtomicInteger()

      private fun Request.uuidRequest(): Request {
         return if (tag(RequestSession::class.java) == null) {
            newBuilder()
               .tag(RequestSession::class.java, RequestSession(uuid = newUUID()))
               .build()
         } else {
            this
         }
      }

      private fun newUUID(): String {
         val uuid = if (sUUID.compareAndSet(Int.MAX_VALUE, 0)) {
            0
         } else {
            sUUID.incrementAndGet()
         }
         return uuid.toString()
      }
   }
}
