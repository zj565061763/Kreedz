package com.sd.android.kreedz.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import com.sd.lib.compose.refresh.FRefreshContainer
import com.sd.lib.compose.refresh.rememberFRefreshStateTop
import com.sd.lib.compose.refresh.setRefreshing

@Composable
fun AppPullToRefresh(
  modifier: Modifier = Modifier,
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  enabled: Boolean = true,
  statusBarsPadding: Boolean = false,
  content: @Composable BoxScope.() -> Unit,
) {
  val refreshState = rememberFRefreshStateTop(
    enabled = enabled,
    onRefresh = onRefresh
  ).apply {
    setRefreshing(isRefreshing)
  }

  Box(
    modifier = modifier.nestedScroll(refreshState.nestedScrollConnection),
    contentAlignment = Alignment.Center,
  ) {
    content()
    FRefreshContainer(
      state = refreshState,
      getRefreshThreshold = {
        if (statusBarsPadding) {
          val density = LocalDensity.current
          it.height + WindowInsets.statusBars.getTop(density)
        } else {
          it.height
        }.toFloat()
      },
      modifier = Modifier.align(Alignment.TopCenter),
    )
  }
}