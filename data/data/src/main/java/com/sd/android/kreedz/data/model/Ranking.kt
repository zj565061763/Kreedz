package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlayerRankingModel(
  val rank: Int,
  val recordNumber: String,
  val percentNumber: String,
  val playerName: String,
  val country: String?,
  val playerId: String,
)

@Immutable
data class CountryRankingModel(
  val rank: Int,
  val recordNumber: String,
  val percentNumber: String,
  val country: String?,
  val countryName: String?,
)