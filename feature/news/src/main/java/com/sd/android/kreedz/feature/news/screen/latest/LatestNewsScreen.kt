package com.sd.android.kreedz.feature.news.screen.latest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.lib.kmp.paging.compose.presenter

@Composable
fun LatestNewsScreen(
  modifier: Modifier = Modifier,
  vm: LatestNewsVM = viewModel(),
) {
  val paging = vm.paging.presenter()
  val context = LocalContext.current

  LaunchedEffect(paging) {
    paging.refresh()
  }

  LatestNewsScreenView(
    modifier = modifier,
    paging = paging,
    onClickItem = {
      AppRouter.news(context, it)
    },
  )
}