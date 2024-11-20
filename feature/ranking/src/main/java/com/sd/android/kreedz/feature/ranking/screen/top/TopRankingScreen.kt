package com.sd.android.kreedz.feature.ranking.screen.top

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.data.model.TopCountryRankingModel
import com.sd.android.kreedz.data.model.TopPlayerRankingModel
import com.sd.android.kreedz.feature.common.ui.ComEffectError

@Composable
fun TopRankingScreen(
   modifier: Modifier = Modifier,
   vm: TopRankingVM = viewModel(),
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val context = LocalContext.current

   AppPullToRefresh(
      modifier = modifier.fillMaxSize(),
      isRefreshing = state.isLoading,
      onRefresh = { vm.refresh() },
   ) {
      ListView(
         topPlayer = state.topPlayer,
         topCountry = state.topCountry,
         onClickPlayer = {
            AppRouter.user(context, it.playerId)
         },
         onClickMorePlayer = {
            AppRouter.playerRanking(context)
         },
         onClickMoreCountry = {
            AppRouter.countryRanking(context)
         },
      )
   }

   LaunchedEffect(vm) {
      vm.init()
   }

   vm.effectFlow.ComEffectError()
}

@Composable
private fun ListView(
   modifier: Modifier = Modifier,
   topPlayer: List<TopPlayerRankingModel>,
   topCountry: List<TopCountryRankingModel>,
   onClickPlayer: (TopPlayerRankingModel) -> Unit,
   onClickMorePlayer: () -> Unit,
   onClickMoreCountry: () -> Unit,
) {
   LazyColumn(
      modifier = modifier.fillMaxSize(),
      contentPadding = PaddingValues(12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
   ) {
      if (topPlayer.isNotEmpty()) {
         item(
            key = "player",
            contentType = "player",
         ) {
            TopPlayerView(
               topPlayer = topPlayer,
               onClickPlayer = onClickPlayer,
               onClickMore = onClickMorePlayer,
            )
         }
      }

      if (topCountry.isNotEmpty()) {
         item(
            key = "country",
            contentType = "country",
         ) {
            TopRankingCountryView(
               topCountry = topCountry,
               onClickMore = onClickMoreCountry,
            )
         }
      }
   }
}