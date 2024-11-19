package com.sd.android.kreedz.feature.ranking.screen.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.feature.common.ui.ComErrorEffect
import com.sd.android.kreedz.feature.ranking.screen.ranking.RankingItemView
import com.sd.android.kreedz.feature.ranking.screen.ranking.RankingScreenView

@Composable
internal fun PlayerRankingScreen(
   modifier: Modifier = Modifier,
   vm: PlayerRankingVM = viewModel(),
   onClickBack: () -> Unit,
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val context = LocalContext.current

   RankingScreenView(
      modifier = modifier,
      title = "Player",
      isLoading = state.isLoading,
      onSelectDate = { vm.refresh(it) },
      onClickBack = onClickBack,
      onRefresh = { vm.refresh(it) },
   ) {
      items(state.rankings) { item ->
         Card(shape = MaterialTheme.shapes.extraSmall) {
            RankingItemView(
               rank = item.rank,
               country = item.country,
               countryText = item.playerName,
               recordNumber = item.recordNumber,
               percentNumber = item.percentNumber,
               modifier = Modifier.clickable {
                  AppRouter.user(context, item.playerId)
               }
            )
         }
      }
   }

   LaunchedEffect(vm) {
      vm.refresh(null)
   }

   vm.effectFlow.ComErrorEffect()
}