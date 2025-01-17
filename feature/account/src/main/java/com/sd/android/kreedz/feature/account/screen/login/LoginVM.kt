package com.sd.android.kreedz.feature.account.screen.login

import androidx.compose.foundation.text.input.TextFieldState
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.repository.AccountRepository
import com.sd.lib.kmp.coroutines.FLoader

internal class LoginVM : BaseViewModel<LoginVM.State, Any>(State()) {
  private val _repository = AccountRepository()
  private val _loginLoader = FLoader()

  val usernameState = TextFieldState()
  val passwordState = TextFieldState()

  fun login() {
    vmLaunch {
      val username = usernameState.text.toString()
      if (username.isBlank()) return@vmLaunch

      val password = passwordState.text.toString()
      if (password.isBlank()) return@vmLaunch

      _loginLoader.tryLoad {
        _repository.login(
          username = username,
          password = password,
        )
      }.onSuccess {
        updateState {
          it.copy(isLoginSuccess = true)
        }
      }.onFailure { error ->
        sendEffect(error)
      }
    }
  }

  fun cancelLogin() {
    vmLaunch {
      _loginLoader.cancel()
    }
  }

  init {
    vmLaunch {
      _loginLoader.loadingFlow.collect { data ->
        updateState {
          it.copy(isLoggingIn = data)
        }
      }
    }

    vmLaunch {
      val lastLoginUsername = _repository.getLastLoginUsername()
      if (lastLoginUsername.isNotBlank()) {
        usernameState.edit {
          if (length == 0) {
            append(lastLoginUsername)
          }
        }
      }
    }
  }

  data class State(
    val isLoggingIn: Boolean = false,
    val isLoginSuccess: Boolean = false,
  )
}