package com.sd.android.kreedz.feature.blog.screen.blog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.feature.news.screen.news.NewsScreen

@Composable
internal fun BlogScreen(
   modifier: Modifier = Modifier,
   id: String,
   vm: BlogVM = viewModel(),
   commentVM: BlogCommentVM = viewModel(),
   onClickBack: () -> Unit,
   onClickOpenUri: (blogId: String) -> Unit,
) {
   NewsScreen(
      modifier = modifier,
      id = id,
      vm = vm,
      commentVM = commentVM,
      onClickBack = onClickBack,
      onClickOpenUri = onClickOpenUri,
   )
}