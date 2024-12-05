package com.sd.android.kreedz.data.network.http.interceptor

import com.sd.android.kreedz.data.network.ModuleNetwork
import com.sd.android.kreedz.data.network.http.AppApi
import com.sd.lib.xlog.ld
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

internal abstract class AppApiInterceptor : Interceptor {

  final override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val api = request.tag(Invocation::class.java)?.method()?.getAnnotation(AppApi::class.java)
    return if (api == null) {
      chain.proceed(request)
    } else {
      interceptImpl(chain, api)
    }
  }

  protected abstract fun interceptImpl(chain: Interceptor.Chain, annotation: AppApi): Response

  /**
   * 打印调试日志
   */
  protected inline fun Request.logDebug(block: () -> Any) {
    ModuleNetwork.ld {
      val uuid = tag(RequestSession::class.java)?.uuid ?: ""
      "http:${uuid} ${block()}"
    }
  }
}

internal data class RequestSession(
  val uuid: String,
)