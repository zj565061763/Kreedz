package com.sd.android.kreedz.data.utils

object FDurationUtils {
   /** 1秒的毫秒数  */
   private const val MILLIS_SECOND: Long = 1000
   /** 1分钟的毫秒数  */
   private const val MILLIS_MINUTES = MILLIS_SECOND * 60
   /** 1小时的毫秒数  */
   private const val MILLIS_HOUR = MILLIS_MINUTES * 60
   /** 1天的毫秒数  */
   private const val MILLIS_DAY = MILLIS_HOUR * 24

   /** 时:分:秒 */
   @JvmStatic
   @JvmOverloads
   inline fun hhmmss(
      ms: Long,
      formatHours: (Long) -> String = { leadingZero(it) },
      formatMinutes: (Long) -> String = { leadingZero(it) },
      formatSeconds: (Long) -> String = { leadingZero(it) },
      separator: String = ":",
   ): String {
      val hours = totalHours(ms)
      val minutes = partOfMinutes(ms)
      val seconds = partOfSeconds(ms)
      return "${formatHours(hours)}${separator}${formatMinutes(minutes)}${separator}${formatSeconds(seconds)}"
   }

   /** 分:秒 */
   @JvmStatic
   @JvmOverloads
   inline fun mmss(
      ms: Long,
      formatMinutes: (Long) -> String = { leadingZero(it) },
      formatSeconds: (Long) -> String = { leadingZero(it) },
      separator: String = ":",
   ): String {
      val minutes = totalMinutes(ms)
      val seconds = partOfSeconds(ms)
      return "${formatMinutes(minutes)}${separator}${formatSeconds(seconds)}"
   }

   /** 分:秒.毫秒 */
   @JvmStatic
   @JvmOverloads
   fun mmssmm(
      ms: Long,
      formatMinutes: (Long) -> String = { leadingZero(it) },
      formatSeconds: (Long) -> String = { leadingZero(it) },
      formatMillisSeconds: (Long) -> String = { leadingZero(it) },
      separator: String = ":",
   ): String {
      val minutes = totalMinutes(ms)
      val seconds = partOfSeconds(ms)
      val millisSeconds = partOfMillisSeconds(ms) / 10
      return "${formatMinutes(minutes)}${separator}${formatSeconds(seconds)}.${formatMillisSeconds(millisSeconds)}"
   }

   @JvmStatic
   fun totalDays(mss: Long): Long = mss / MILLIS_DAY
   @JvmStatic
   fun totalHours(ms: Long): Long = ms / MILLIS_HOUR
   @JvmStatic
   fun totalMinutes(ms: Long): Long = ms / MILLIS_MINUTES

   @JvmStatic
   fun partOfHours(ms: Long): Long = (ms % MILLIS_DAY) / MILLIS_HOUR
   @JvmStatic
   fun partOfMinutes(ms: Long): Long = (ms % MILLIS_HOUR) / MILLIS_MINUTES
   @JvmStatic
   fun partOfSeconds(ms: Long): Long = (ms % MILLIS_MINUTES) / MILLIS_SECOND
   @JvmStatic
   fun partOfMillisSeconds(ms: Long): Long = ms % MILLIS_SECOND

   fun leadingZero(input: Long, size: Int = 2): String {
      return input.toString().let {
         val repeat = size - it.length
         if (repeat > 0) "0".repeat(repeat) + it
         else it
      }
   }
}