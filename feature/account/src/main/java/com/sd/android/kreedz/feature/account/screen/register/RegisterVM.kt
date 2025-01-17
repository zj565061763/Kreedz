package com.sd.android.kreedz.feature.account.screen.register

import androidx.compose.foundation.text.input.TextFieldState
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.core.utils.AppUtils
import com.sd.android.kreedz.data.repository.AccountRepository
import com.sd.lib.kmp.coroutines.FLoader

internal class RegisterVM : BaseViewModel<RegisterVM.State, Any>(State()) {
  private val _repository = AccountRepository()
  private val _registerLoader = FLoader()

  val emailState = TextFieldState()
  val nicknameState = TextFieldState()
  val usernameState = TextFieldState()
  val passwordState = TextFieldState()
  val confirmPasswordState = TextFieldState()

  fun register() {
    vmLaunch {
      val email = emailState.text.toString()
      if (!AppUtils.isValidEmail(email)) {
        sendEffect("Invalid email")
        return@vmLaunch
      }

      val nickname = nicknameState.text.toString()
      if (nickname.isBlank()) return@vmLaunch

      val username = usernameState.text.toString()
      if (username.isBlank()) return@vmLaunch

      val password = passwordState.text.toString()
      if (password.isEmpty()) return@vmLaunch

      val confirmPassword = confirmPasswordState.text.toString()
      if (confirmPassword != password) {
        sendEffect("Passwords do not match!")
        return@vmLaunch
      }

      _registerLoader.load {
        _repository.register(
          email = email,
          nickname = nickname,
          username = username,
          password = password,
        )
      }.onSuccess {
        updateState {
          it.copy(isRegisterSuccess = true)
        }
      }.onFailure { error ->
        sendEffect(error)
      }
    }
  }

  fun cancelRegister() {
    vmLaunch {
      _registerLoader.cancel()
    }
  }

  init {
    vmLaunch {
      _registerLoader.loadingFlow.collect { data ->
        updateState {
          it.copy(isRegistering = data)
        }
      }
    }
  }

  data class State(
    val isRegistering: Boolean = false,
    val isRegisterSuccess: Boolean = false,
  )
}