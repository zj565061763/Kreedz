package com.sd.android.kreedz.feature.common.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sd.android.kreedz.data.network.exception.HttpMessageException
import com.sd.android.kreedz.data.network.exception.HttpTooManyRequestsException
import com.sd.android.kreedz.data.network.exception.HttpUnauthorizedException
import com.sd.lib.retry.ktx.RetryMaxCountException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

@Composable
fun Flow<*>.ComEffect() {
  val flow = this

  var effectError by remember { mutableStateOf<Throwable?>(null) }
  var effectString by remember { mutableStateOf("") }

  LaunchedEffect(flow) {
    flow.collect { effect ->
      when {
        effect is Throwable && !effect.isCancellationException() -> {
          effectError = effect
        }
        effect is String -> {
          effectString = effect
        }
      }
    }
  }

  if (effectString.isNotBlank()) {
    ComAlertDialog(
      text = {
        Text(text = effectString)
      },
      onDismissRequest = { effectString = "" },
    )
  }

  effectError?.also { error ->
    ComErrorDialog(
      error = remember(error) { error.errorMessage() },
      onDismissRequest = { effectError = null },
      onClickRetry = null,
    )
  }
}

private fun Throwable.errorMessage(): String {
  return when (this) {
    is HttpUnauthorizedException -> "Unauthorized!"
    is HttpMessageException -> message
    is HttpTooManyRequestsException -> "Too Many Requests! Please retry later."
    is RetryMaxCountException -> cause.toString()
    else -> toString()
  }
}

private fun Throwable.isCancellationException(): Boolean {
  return this is CancellationException || this.cause is CancellationException
}