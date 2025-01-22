package com.sd.android.kreedz.feature.account.screen.recover

import android.os.Bundle
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.data.repository.AccountRepository
import com.sd.android.kreedz.feature.common.ui.ComAlertDialog
import com.sd.android.kreedz.feature.common.ui.ComEffect

@Router(path = AppRouter.RECOVER_USERNAME)
internal class RecoverUsernameActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setPageContent {
      val vm = viewModel<RecoverUsernameVM>()
      val state by vm.stateFlow.collectAsStateWithLifecycle()

      RecoverScreenView(
        title = "Recover Username",
        emailState = vm.emailState,
        isLoading = state.isLoading,
        onCancelLoading = { vm.cancelRecover() },
        onClickBack = {
          finish()
        },
        onClickRecover = {
          vm.recover()
        },
      )

      if (state.isLoadingSuccess) {
        ComAlertDialog(
          onDismissRequest = { finish() },
          text = {
            Text(
              text = "Username recovery asked - check your mail (and spams)"
            )
          },
        )
      }

      vm.effectFlow.ComEffect()
    }
  }
}

internal class RecoverUsernameVM : RecoverVM() {
  private val _repository = AccountRepository()

  override suspend fun recover(email: String) {
    _repository.recoverUsername(email)
  }
}