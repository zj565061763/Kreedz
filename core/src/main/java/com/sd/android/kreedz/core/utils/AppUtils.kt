package com.sd.android.kreedz.core.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.sd.lib.ctx.fContext

object AppUtils {

  fun copyText(
    text: String,
    label: CharSequence = "",
  ): Boolean {
    return runCatching {
      val data = ClipData.newPlainText(label, text)
      val manager = fContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      manager.setPrimaryClip(data)
    }.isSuccess
  }

  fun isValidEmail(email: String): Boolean {
    if (email.isBlank()) return false
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    return emailRegex.matches(email)
  }
}