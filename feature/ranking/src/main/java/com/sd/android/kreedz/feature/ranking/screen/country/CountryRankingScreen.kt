package com.sd.android.kreedz.feature.ranking.screen.country

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.feature.common.ui.ComEffect
import com.sd.android.kreedz.feature.ranking.screen.ranking.RankingItemView
import com.sd.android.kreedz.feature.ranking.screen.ranking.RankingScreenView

@Composable
internal fun CountryRankingScreen(
   modifier: Modifier = Modifier,
   vm: CountryRankingVM = viewModel(),
   onClickBack: () -> Unit,
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()

   RankingScreenView(
      modifier = modifier,
      title = "Country",
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
               countryText = item.countryName,
               recordNumber = item.recordNumber,
               percentNumber = item.percentNumber,
            )
         }
      }
   }

   LaunchedEffect(vm) {
      vm.refresh(null)
   }

   vm.effectFlow.ComEffect()
}