package com.sd.android.kreedz.feature.ranking.screen.country

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.CountryRankingModel
import com.sd.android.kreedz.data.repository.RankingRepository
import com.sd.lib.coroutines.FLoader
import com.sd.lib.date.FDate

internal class CountryRankingVM : BaseViewModel<CountryRankingVM.State, Any>(State()) {
   private val _repository = RankingRepository()
   private val _loader = FLoader()

   fun refresh(date: FDate?) {
      vmLaunch {
         _loader.load {
            val rankings = _repository.getCountryRanking(date?.toString())
            updateState {
               it.copy(rankings = rankings)
            }
         }.onFailure { error ->
            sendEffect(error)
         }
      }
   }

   init {
      vmLaunch {
         _loader.loadingFlow.collect { data ->
            updateState {
               it.copy(isLoading = data)
            }
         }
      }
   }

   @Immutable
   data class State(
      val isLoading: Boolean = false,
      val rankings: List<CountryRankingModel> = emptyList(),
   )
}