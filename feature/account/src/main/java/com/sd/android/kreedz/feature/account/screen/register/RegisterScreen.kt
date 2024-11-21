package com.sd.android.kreedz.feature.account.screen.register

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.feature.common.ui.ComEffect
import com.sd.android.kreedz.feature.common.ui.ComLoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RegisterScreen(
   modifier: Modifier = Modifier,
   vm: RegisterVM = viewModel(),
   onClickBack: () -> Unit,
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()

   Scaffold(
      modifier = modifier,
      topBar = {
         TopAppBar(
            title = {
               Text(text = "Register")
            },
            navigationIcon = {
               IconButton(onClick = onClickBack) {
                  Icon(
                     imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                     contentDescription = "Back",
                  )
               }
            },
         )
      }
   ) { padding ->
      RegisterInputView(
         modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .padding(16.dp),
         emailState = vm.emailState,
         nicknameState = vm.nicknameState,
         usernameState = vm.usernameState,
         passwordState = vm.passwordState,
         confirmPasswordState = vm.confirmPasswordState,
         onClickRegister = {
            vm.register()
         },
      )
   }

   if (state.isRegistering) {
      ComLoadingDialog {
         vm.cancelRegister()
      }
   }

   val onClickBackUpdated by rememberUpdatedState(onClickBack)
   if (state.isRegisterSuccess) {
      LaunchedEffect(Unit) {
         onClickBackUpdated()
      }
   }

   vm.effectFlow.ComEffect()
}