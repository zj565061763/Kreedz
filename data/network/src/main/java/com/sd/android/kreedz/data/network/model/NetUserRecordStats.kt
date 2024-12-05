package com.sd.android.kreedz.data.network.model

data class NetUserRecordStats(
  val rank: String?,
  val numberCurrentRecords: String,
  val numberTotalRecords: String,
  val numberDifferentMaps: String,
  val firstRecord: NetUserRecord?,
  val lastRecord: NetUserRecord?,
  val currentRecords: List<NetUserRecord>?,
  val previousRecords: List<NetUserRecord>?,
)

data class NetUserRecord(
  val mapName: String,
  val time: String,
  val date: String,
  val mapId: String?,
  val timeDifference: String?,
)