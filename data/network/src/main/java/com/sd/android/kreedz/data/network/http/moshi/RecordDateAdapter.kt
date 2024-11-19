package com.sd.android.kreedz.data.network.http.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Locale

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class RecordDate

internal class RecordDateAdapter {
   @RecordDate
   @FromJson
   fun fromJson(value: String?): Long {
      if (value.isNullOrBlank()) return 0
      val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
      return runCatching {
         format.parse(value)?.time ?: 0
      }.getOrDefault(0)
   }

   @ToJson
   fun toJson(@RecordDate value: Long): String {
      return value.toString()
   }
}