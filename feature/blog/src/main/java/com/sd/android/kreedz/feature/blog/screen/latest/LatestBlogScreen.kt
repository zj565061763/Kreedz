package com.sd.android.kreedz.feature.blog.screen.latest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.feature.news.screen.latest.LatestNewsScreenView
import com.sd.lib.paging.compose.presenter

@Composable
fun LatestBlogScreen(
  modifier: Modifier = Modifier,
  vm: LatestBlogVM = viewModel(),
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
      AppRouter.blog(context, it)
    },
  )
}