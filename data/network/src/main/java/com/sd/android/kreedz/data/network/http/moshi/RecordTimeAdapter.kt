package com.sd.android.kreedz.data.network.http.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class RecordTime

internal class RecordTimeAdapter {
  @RecordTime
  @FromJson
  fun fromJson(value: String?): Long {
    if (value.isNullOrBlank()) return 0
    if (!value.contains(":")) return 0

    val parts = value.split(":").reversed()
    val seconds = parts[0].toFloatOrNull() ?: return 0
    val minutes = parts[1].toIntOrNull() ?: return 0

    return (seconds * 1000).toLong() + (minutes * 60 * 1000)
  }

  @ToJson
  fun toJson(@RecordTime value: Long): String {
    return value.toString()
  }
}