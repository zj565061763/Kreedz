package com.sd.android.kreedz.feature.account.screen.recover

import androidx.compose.foundation.text.input.TextFieldState
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.lib.coroutines.FLoader
import com.sd.lib.coroutines.tryLoad

internal abstract class RecoverVM : BaseViewModel<RecoverVM.State, Any>(State()) {
   private val _loader = FLoader()

   val emailState = TextFieldState()

   fun recover() {
      vmLaunch {
         val email = emailState.text.toString()
         if (email.isBlank()) return@vmLaunch
         _loader.tryLoad {
            recover(email)
         }.onSuccess {
            updateState {
               it.copy(isLoadingSuccess = true)
            }
         }.onFailure { error ->
            sendEffect(error)
         }
      }
   }

   fun cancelRecover() {
      vmLaunch {
         _loader.cancel()
      }
   }

   protected abstract suspend fun recover(email: String)

   init {
      vmLaunch {
         _loader.loadingFlow.collect { data ->
            updateState {
               it.copy(isLoading = data)
            }
         }
      }
   }

   data class State(
      val isLoading: Boolean = false,
      val isLoadingSuccess: Boolean = false,
   )
}