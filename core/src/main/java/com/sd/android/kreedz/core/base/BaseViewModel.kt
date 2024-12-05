package com.sd.android.kreedz.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sd.lib.xlog.FLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BaseViewModel<S, E>(
  initialState: S,
) : ViewModel(), FLogger {

  private val _stateFlow = MutableStateFlow(initialState)
  private val _effectFlow = MutableSharedFlow<E>()

  val state: S get() = _stateFlow.value
  val stateFlow: StateFlow<S> = _stateFlow.asStateFlow()
  val effectFlow: Flow<E> = _effectFlow.asSharedFlow()

  protected fun updateState(function: (S) -> S) {
    _stateFlow.update(function)
  }

  protected fun sendEffect(effect: E) {
    vmLaunch {
      _effectFlow.emit(effect)
    }
  }

  protected fun vmLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
  ) {
    viewModelScope.launch(
      context = context,
      start = start,
      block = block,
    )
  }
}