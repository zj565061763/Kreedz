package com.sd.android.kreedz.data.network.http

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class AppApi(
   val transformFormBody: Boolean = true,
   val resultLog: Boolean = true,
)