package com.sd.android.kreedz.data.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DataUtils {
  fun formatRecordTime(time: Long?): String {
    if (time == null || time == 0L) return ""
    return FDurationUtils.mmssmm(time)
  }

  fun formatDate(date: Long?): String {
    if (date == null || date == 0L) return ""
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(date)
  }
}