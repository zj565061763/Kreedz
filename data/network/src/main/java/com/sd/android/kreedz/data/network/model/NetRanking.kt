package com.sd.android.kreedz.data.network.model

data class NetPlayerRanking(
   val rank: Int,
   val recordNumber: String,
   val percentNumber: String,
   val pseudo: String,
   val country: String?,
   val playerId: String,
)

data class NetCountryRanking(
   val rank: Int,
   val recordNumber: String,
   val percentNumber: String,
   val country: String?,
   val countryName: String?,
)