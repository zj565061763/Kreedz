package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ChatBoxItemModel

@Immutable
data class ChatBoxMessageModel(
  val id: String,
  val message: String,
  val dateTimeStr: String,
  val author: UserWithIconsModel,
) : ChatBoxItemModel {
  private val _splits = dateTimeStr.split(" ")
  val dateStr: String = _splits.getOrNull(0) ?: ""
  val timeStr: String = _splits.getOrNull(1) ?: dateTimeStr
}

@Immutable
data class ChatBoxDateModel(
  val dateStr: String,
) : ChatBoxItemModel