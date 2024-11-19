package com.sd.android.kreedz.feature.common.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sd.lib.compose.webview.FWebView
import com.sd.lib.compose.webview.LoadingState
import com.sd.lib.compose.webview.WebViewState
import com.sd.lib.compose.webview.rememberWebViewNavigator
import com.sd.lib.compose.webview.rememberWebViewState
import com.sd.lib.xlog.fDebug

@Composable
internal fun WebScreen(
   modifier: Modifier = Modifier,
   url: String,
   onClickBack: () -> Unit,
) {
   val webviewState = rememberWebViewState()

   Scaffold(
      modifier = modifier,
      topBar = {
         TitleView(
            webviewState = webviewState,
            onClickBack = onClickBack,
         )
      },
   ) { padding ->
      FWebView(
         modifier = Modifier
            .fillMaxSize()
            .padding(padding),
         state = webviewState,
      )
   }

   LaunchedEffect(webviewState, url) {
      webviewState.loadUrl(url)
   }

   LaunchedEffect(webviewState.loadingState) {
      fDebug { "loadingState:${webviewState.loadingState}" }
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleView(
   modifier: Modifier = Modifier,
   webviewState: WebViewState,
   onClickBack: () -> Unit,
) {
   val webViewNavigator = rememberWebViewNavigator()

   Box(modifier = modifier) {
      TopAppBar(
         navigationIcon = {
            IconButton(onClick = {
               if (webViewNavigator.canGoBack) {
                  webViewNavigator.navigateBack()
               } else {
                  onClickBack()
               }
            }) {
               Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
               )
            }
         },
         title = {
            Text(text = webviewState.pageTitle ?: "")
         },
      )
      webviewState.loadingState.also { loadingState ->
         if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
               progress = { loadingState.progress },
               modifier = Modifier
                  .fillMaxWidth()
                  .height(1.dp)
                  .align(Alignment.BottomCenter),
            )
         }
      }
   }
}