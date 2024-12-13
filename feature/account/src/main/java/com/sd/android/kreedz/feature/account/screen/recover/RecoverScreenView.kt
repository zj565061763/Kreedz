package com.sd.android.kreedz.feature.account.screen.recover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sd.android.kreedz.feature.common.ui.ComLoadingLayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecoverScreenView(
  modifier: Modifier = Modifier,
  title: String,
  emailState: TextFieldState,
  isLoading: Boolean,
  onCancelLoading: () -> Unit,
  onClickBack: () -> Unit,
  onClickRecover: () -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(text = title)
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
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState())
    ) {
      RecoverInputView(
        emailState = emailState,
        onClickRecover = onClickRecover,
        modifier = Modifier.padding(16.dp),
      )
    }
  }

  ComLoadingLayer(
    attach = isLoading,
    onDetachRequest = onCancelLoading,
  )
}