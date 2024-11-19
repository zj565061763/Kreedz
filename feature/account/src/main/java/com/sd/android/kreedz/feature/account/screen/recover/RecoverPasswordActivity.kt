package com.sd.android.kreedz.feature.account.screen.recover

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.didi.drouter.annotation.Router
import com.sd.android.kreedz.core.base.BaseActivity
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.data.repository.AccountRepository
import com.sd.android.kreedz.feature.common.ui.ComAlertDialog
import com.sd.android.kreedz.feature.common.ui.ComErrorEffect

@Router(path = AppRouter.RECOVER_PASSWORD)
internal class RecoverPasswordActivity : BaseActivity() {
   @Composable
   override fun ContentImpl() {
      val vm = viewModel<RecoverPasswordVM>()
      val state by vm.stateFlow.collectAsStateWithLifecycle()

      RecoverScreenView(
         title = "Recover Password",
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
                  text = "Password recover asked - check your mail (and spams)"
               )
            },
         )
      }

      vm.effectFlow.ComErrorEffect()
   }
}

internal class RecoverPasswordVM : RecoverVM() {
   private val _repository = AccountRepository()

   override suspend fun recover(email: String) {
      _repository.recoverPassword(email)
   }
}