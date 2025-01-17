package com.sd.android.kreedz.data.network

import com.sd.android.kreedz.data.network.model.NetOnlineUsers
import com.sd.lib.kmp.coroutines.FLoader
import com.sd.lib.kmp.coroutines.fGlobalLaunch
import com.sd.lib.lifecycle.fAppOnStart
import com.sd.lib.lifecycle.fAppOnStop
import com.sd.lib.lifecycle.fAwaitAppStarted
import com.sd.lib.moshi.fMoshi
import com.sd.lib.network.fAwaitNetwork
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.ld
import com.sd.lib.xlog.li
import com.sd.lib.xlog.lw
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

object NetOnlineUsersDataSource : FLogger {
  private var _userId: String? = null
  private var _connectUserId: String? = null

  private val _onlineUsersFlow = MutableStateFlow<NetOnlineUsers?>(null)
  private val _connectLoader = FLoader()

  val onlineUsersFlow: Flow<NetOnlineUsers?>
    get() = _onlineUsersFlow.asStateFlow()

  fun setUserId(userId: String) {
    NetOnlineUsersDataSource
    li { "setUserId:$userId" }
    _userId = userId
    connect()
  }

  private fun retryConnect() {
    fGlobalLaunch {
      li { "retryConnect" }
      _connectLoader.load {
        delay(10_000)
        connect(_userId)
      }
    }
  }

  private fun connect() {
    fGlobalLaunch {
      if (_connectLoader.isLoading && _connectUserId == _userId) {
        return@fGlobalLaunch
      }
      _connectLoader.load {
        connect(_userId)
      }
    }
  }

  private fun disconnect() {
    fGlobalLaunch {
      li { "disconnect" }
      _connectLoader.cancel()
    }
  }

  private suspend fun connect(userId: String?) {
    if (userId == null) return
    li { "connect (${userId})" }

    _connectUserId = userId
    fAwaitAppStarted()
    fAwaitNetwork()

    newWebSocketFlow(userId).collect {
      _onlineUsersFlow.value = it
    }
  }

  private fun newWebSocketFlow(userId: String): Flow<NetOnlineUsers?> {
    return callbackFlow {
      val socket = ModuleNetwork.connectOnlineUsersWebSocket(
        userId = userId,
        listener = object : InternalWebSocketListener(
          userId = userId,
          logger = this@NetOnlineUsersDataSource,
        ) {
          override fun onData(data: NetOnlineUsers?) {
            trySendBlocking(data)
          }

          override fun onFailure(t: Throwable) {
            close()
            retryConnect()
          }

          override fun onClosed() {
            _onlineUsersFlow.value = null
            close()
          }
        }
      )
      awaitClose {
        runCatching {
          li { "(${userId}) close socket" }
          socket.close(1000, "")
        }.onFailure { error ->
          lw { "(${userId}) close socket error:${error.stackTraceToString()}" }
        }
      }
    }
  }

  init {
    fGlobalLaunch {
      fAppOnStart {
        connect()
      }
    }
    fGlobalLaunch {
      fAppOnStop {
        disconnect()
      }
    }
  }
}

private abstract class InternalWebSocketListener(
  private val userId: String,
  private val logger: FLogger,
) : WebSocketListener() {
  private val _jsonAdapter = fMoshi.adapter(NetOnlineUsers::class.java)

  final override fun onOpen(webSocket: WebSocket, response: Response) {
    logger.li { "(${userId}) onOpen" }
  }

  final override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
    logger.ld { "(${userId}) onMessage bytes:${bytes.size}" }
  }

  final override fun onMessage(webSocket: WebSocket, text: String) {
    logger.ld { "(${userId}) onMessage:${text}" }
    runCatching {
      _jsonAdapter.fromJson(text)
    }.onSuccess { data ->
      onData(data)
    }.onFailure { error ->
      onData(null)
      logger.lw { "(${userId}) onMessage json error:${error.stackTraceToString()}" }
    }
  }

  final override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    logger.li { "(${userId}) onClosing code:${code}|reason:${reason}" }
  }

  final override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
    logger.li { "(${userId}) onClosed code:${code}|reason:${reason}" }
    onClosed()
  }

  final override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
    logger.lw { "(${userId}) onFailure ${t.stackTraceToString()}" }
    onFailure(t)
  }

  protected abstract fun onData(data: NetOnlineUsers?)
  protected abstract fun onFailure(t: Throwable)
  protected abstract fun onClosed()
}