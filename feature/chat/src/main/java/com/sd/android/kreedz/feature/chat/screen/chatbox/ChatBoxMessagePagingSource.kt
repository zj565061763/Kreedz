package com.sd.android.kreedz.feature.chat.screen.chatbox

import com.sd.android.kreedz.data.model.ChatBoxItemModel
import com.sd.android.kreedz.data.repository.ChatBoxRepository
import com.sd.lib.paging.KeyIntPagingSource
import com.sd.lib.paging.LoadParams
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.ld

internal class ChatBoxMessagePagingSource : KeyIntPagingSource<ChatBoxItemModel>(), FLogger {
  private val _repository = ChatBoxRepository()

  override suspend fun loadImpl(params: LoadParams<Int>): List<ChatBoxItemModel> {
    ld { "load $params" }
    return _repository.messages(params.key)
  }
}