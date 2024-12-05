package com.sd.android.kreedz.feature.chat.screen.chatbox

import com.sd.android.kreedz.data.model.ChatBoxMessageModel
import com.sd.android.kreedz.data.repository.ChatBoxRepository
import com.sd.lib.compose.paging.FIntPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ChatBoxMessagePagingSource : FIntPagingSource<ChatBoxMessageModel>(
  initialKey = 0,
) {
  private val _repository = ChatBoxRepository()

  override suspend fun loadImpl(params: LoadParams<Int>, key: Int): List<ChatBoxMessageModel> {
    val data = _repository.messages(key)
    return withContext(Dispatchers.IO) {
      data.filter { it.id.isNotBlank() }
    }
  }
}