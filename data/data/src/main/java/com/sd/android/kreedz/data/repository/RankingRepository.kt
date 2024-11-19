package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.model.CountryRankingModel
import com.sd.android.kreedz.data.model.PlayerRankingModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetCountryRanking
import com.sd.android.kreedz.data.network.model.NetPlayerRanking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun RankingRepository(): RankingRepository = RankingRepositoryImpl()

interface RankingRepository {
   suspend fun getPlayerRanking(date: String?): List<PlayerRankingModel>
   suspend fun getCountryRanking(date: String?): List<CountryRankingModel>
}

private class RankingRepositoryImpl : RankingRepository {
   private val _netDataSource = NetDataSource()

   override suspend fun getPlayerRanking(date: String?): List<PlayerRankingModel> {
      return _netDataSource.getPlayerRanking(date)
         .let { data ->
            withContext(Dispatchers.IO) {
               data.map { it.asPlayerRankingModel() }
            }
         }
   }

   override suspend fun getCountryRanking(date: String?): List<CountryRankingModel> {
      return _netDataSource.getCountryRanking(date)
         .let { data ->
            withContext(Dispatchers.IO) {
               data.map { it.asCountryRankingModel() }
            }
         }
   }
}

private fun NetPlayerRanking.asPlayerRankingModel(): PlayerRankingModel {
   return PlayerRankingModel(
      rank = rank,
      recordNumber = recordNumber,
      percentNumber = percentNumber,
      country = country,
      playerName = pseudo,
      playerId = playerId,
   )
}

private fun NetCountryRanking.asCountryRankingModel(): CountryRankingModel {
   return CountryRankingModel(
      rank = rank,
      recordNumber = recordNumber,
      percentNumber = percentNumber,
      country = country,
      countryName = countryName,
   )
}