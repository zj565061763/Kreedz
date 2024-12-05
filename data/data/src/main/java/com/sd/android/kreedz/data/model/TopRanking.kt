package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class TopPlayerRankingModel(
  val playerCountry: String?,
  val playerName: String,
  val playerId: String,
  val numberOfRecords: String,
)

@Immutable
data class TopCountryRankingModel(
  val country: String?,
  val countryName: String,
  val numberOfRecords: String,
)