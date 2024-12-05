package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.model.TopCountryRankingModel
import com.sd.android.kreedz.data.model.TopPlayerRankingModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetTopCountryRanking
import com.sd.android.kreedz.data.network.model.NetTopPlayerRanking
import com.sd.lib.coroutines.FSyncable
import com.sd.lib.coroutines.syncOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

fun TopRankingRepository(): TopRankingRepository = TopRankingRepositoryImpl

interface TopRankingRepository {
  fun getTopPlayerRankingFlow(): Flow<List<TopPlayerRankingModel>>
  fun getTopCountryRankingFlow(): Flow<List<TopCountryRankingModel>>
  suspend fun sync()
}

private object TopRankingRepositoryImpl : TopRankingRepository {
  private val _netDataSource = NetDataSource()
  private val _topPlayerFlow = MutableStateFlow<List<TopPlayerRankingModel>>(emptyList())
  private val _topCountryFlow = MutableStateFlow<List<TopCountryRankingModel>>(emptyList())

  override fun getTopPlayerRankingFlow(): Flow<List<TopPlayerRankingModel>> {
    return _topPlayerFlow.asStateFlow()
  }

  override fun getTopCountryRankingFlow(): Flow<List<TopCountryRankingModel>> {
    return _topCountryFlow.asStateFlow()
  }

  override suspend fun sync() {
    _syncable.syncOrThrow()
  }

  private val _syncable = FSyncable {
    val data = _netDataSource.getTopRanking()
    supervisorScope {
      launch {
        withContext(Dispatchers.IO) {
          data.playerTop5
            .take(5)
            .map { it.asTopPlayerRankingModel() }
        }.also {
          _topPlayerFlow.value = it
        }
      }
      launch {
        withContext(Dispatchers.IO) {
          data.countryTop5
            .take(5)
            .map { it.asTopCountryRankingModel() }
        }.also {
          _topCountryFlow.value = it
        }
      }
    }
  }
}

private fun NetTopPlayerRanking.asTopPlayerRankingModel(): TopPlayerRankingModel {
  return TopPlayerRankingModel(
    playerCountry = country,
    playerName = playerName,
    playerId = playerId,
    numberOfRecords = numberOfRecords,
  )
}

private fun NetTopCountryRanking.asTopCountryRankingModel(): TopCountryRankingModel {
  return TopCountryRankingModel(
    country = country,
    countryName = countryName,
    numberOfRecords = numberOfCountryRecords,
  )
}