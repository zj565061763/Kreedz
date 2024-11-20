package com.sd.android.kreedz.feature.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sd.android.kreedz.data.network.exception.HttpMessageException
import com.sd.android.kreedz.data.network.exception.HttpUnauthorizedException
import com.sd.lib.retry.ktx.RetryMaxCountException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

@Composable
fun Flow<*>.ComEffectError() {
   val flow = this
   var throwable by remember { mutableStateOf<Throwable?>(null) }

   LaunchedEffect(flow) {
      flow.collect { effect ->
         if (effect is Throwable
            && !effect.isCancellationException()
         ) {
            throwable = effect
         }
      }
   }

   throwable?.also { error ->
      ComErrorDialog(
         error = remember(error) { error.errorMessage() },
         onDismissRequest = { throwable = null },
         onClickRetry = null,
      )
   }
}

private fun Throwable.errorMessage(): String {
   return when (this) {
      is HttpUnauthorizedException -> "Unauthorized!"
      is HttpMessageException -> message
      is RetryMaxCountException -> cause.toString()
      else -> toString()
   }
}

private fun Throwable.isCancellationException(): Boolean {
   return this is CancellationException || this.cause is CancellationException
}