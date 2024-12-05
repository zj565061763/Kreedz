package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.mapper.asUserIconsModel
import com.sd.android.kreedz.data.model.ChatBoxMessageModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetChatBoxMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun ChatBoxRepository(): ChatBoxRepository = ChatBoxRepositoryImpl()

interface ChatBoxRepository {
  suspend fun messages(page: Int): List<ChatBoxMessageModel>
  suspend fun sendMessage(content: String)
  suspend fun deleteMessage(id: String)
}

private class ChatBoxRepositoryImpl : ChatBoxRepository {
  private val _netDataSource = NetDataSource()

  override suspend fun messages(page: Int): List<ChatBoxMessageModel> {
    val data = _netDataSource.chatBoxMessages(page)
    return withContext(Dispatchers.IO) {
      data.map { it.asMessageModel() }
    }
  }

  override suspend fun sendMessage(content: String) {
    _netDataSource.chatBoxSendMessage(content)
  }

  override suspend fun deleteMessage(id: String) {
    _netDataSource.chatBoxDeleteMessage(id)
  }
}

private fun NetChatBoxMessage.asMessageModel(): ChatBoxMessageModel {
  return ChatBoxMessageModel(
    id = id,
    message = message,
    dateTimeStr = commentDate,
    author = UserWithIconsModel(
      id = authorId ?: "",
      nickname = author,
      country = country,
      icons = icons.asUserIconsModel(),
    ),
  )
}