package com.sd.android.kreedz.data.network.http.interceptor

import com.sd.android.kreedz.data.network.ModuleNetwork
import com.sd.android.kreedz.data.network.event.EHttpUnauthorized
import com.sd.android.kreedz.data.network.exception.HttpUnauthorizedException
import com.sd.android.kreedz.data.network.http.AppApi
import com.sd.lib.coroutines.FContinuations
import com.sd.lib.event.FEvent
import com.sd.lib.retry.ktx.fNetRetry
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.closeQuietly
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

internal class AuthInterceptor : AppApiInterceptor() {
   private var _isRefreshToken = AtomicBoolean()
   private val _continuations = FContinuations<Unit>()

   override fun interceptImpl(chain: Interceptor.Chain, annotation: AppApi): Response {
      val request = chain.request()
      val response = chain.proceed(request)
      return if (response.code == 401) {
         runBlocking { handleUnauthorized(chain, request) }
      } else {
         response
      }
   }

   private suspend fun handleUnauthorized(
      chain: Interceptor.Chain,
      request: Request,
   ): Response {
      return if (_isRefreshToken.compareAndSet(false, true)) {
         try {
            refreshToken(chain, request).let { success ->
               if (success) {
                  _continuations.resumeAll(Unit)
                  chain.proceed(request)
               } else {
                  FEvent.post(EHttpUnauthorized())
                  throw HttpUnauthorizedException()
               }
            }
         } finally {
            _isRefreshToken.set(false)
            _continuations.cancelAll()
         }
      } else {
         runCatching {
            request.logDebug { "awaitRefreshToken" }
            _continuations.await()
            request.logDebug { "awaitRefreshToken resumed" }
         }.onFailure { e ->
            request.logDebug { "awaitRefreshToken error:${e.stackTraceToString()}" }
            throw IOException(e)
         }
         chain.proceed(request)
      }
   }

   private suspend fun refreshToken(
      chain: Interceptor.Chain,
      request: Request,
   ): Boolean {
      check(_isRefreshToken.get())
      return fNetRetry {
         request.logDebug { "refreshToken" }
         request.newBuilder()
            .url(ModuleNetwork.urlService.getRefreshTokenUrl())
            .method("POST", "".toRequestBody())
            .build()
            .let { chain.proceed(it) }
            .also { it.closeQuietly() }
      }.onSuccess { data ->
         request.logDebug { "refreshToken success code:${data.code}" }
      }.onFailure { error ->
         request.logDebug { "refreshToken error:${error.stackTraceToString()}" }
      }.let {
         it.getOrNull()?.isSuccessful == true
      }
   }
}