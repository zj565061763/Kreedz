package com.sd.android.kreedz.feature.blog.screen.latest

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.feature.news.screen.latest.LatestNewsScreenView

@Composable
fun LatestBlogScreen(
   modifier: Modifier = Modifier,
   vm: LatestBlogVM = viewModel(),
) {
   val items = vm.itemsFlow.collectAsLazyPagingItems()
   val context = LocalContext.current

   LatestNewsScreenView(
      modifier = modifier,
      items = items,
      onClickItem = {
         AppRouter.blog(context, it)
      },
   )
}