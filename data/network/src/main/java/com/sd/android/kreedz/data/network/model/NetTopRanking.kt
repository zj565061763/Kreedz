package com.sd.android.kreedz.data.network.model

data class NetTopRanking(
  val playerTop5: List<NetTopPlayerRanking>,
  val countryTop5: List<NetTopCountryRanking>,
)

data class NetTopPlayerRanking(
  val country: String?,
  val playerName: String,
  val playerId: String,
  val numberOfRecords: String,
)

data class NetTopCountryRanking(
  val country: String?,
  val countryName: String,
  val numberOfCountryRecords: String,
)