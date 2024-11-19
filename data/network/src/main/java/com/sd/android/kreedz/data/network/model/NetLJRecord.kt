package com.sd.android.kreedz.data.network.model

data class NetGroupedLJRecords(
   val type: String,
   val records: List<NetLJRecord>,
)

data class NetLJRecord(
   val pseudo: String,
   val country: String?,
   val block: String,
   val distance: String,
   val prestrafe: String,
   val topspeed: String,
   val ytLink: String?,
)