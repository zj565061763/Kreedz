package com.sd.android.kreedz.feature.news.screen.latest

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.sd.android.kreedz.core.router.AppRouter

@Composable
fun LatestNewsScreen(
  modifier: Modifier = Modifier,
  vm: LatestNewsVM = viewModel(),
) {
  val items = vm.itemsFlow.collectAsLazyPagingItems()
  val context = LocalContext.current

  LatestNewsScreenView(
    modifier = modifier,
    items = items,
    onClickItem = {
      AppRouter.news(context, it)
    },
  )
}