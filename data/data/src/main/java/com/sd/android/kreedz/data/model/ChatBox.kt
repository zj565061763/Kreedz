package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ChatBoxItemModel {
  val dateStr: String
}

@Immutable
data class ChatBoxMessageModel(
  val id: String,
  val message: String,
  val dateTimeStr: String,
  val author: UserWithIconsModel,
) : ChatBoxItemModel {
  override val dateStr: String = dateTimeStr.split(" ").let { if (it.size == 2) it[0] else "" }
}

@Immutable
data class ChatBoxDateModel(
  override val dateStr: String,
) : ChatBoxItemModel