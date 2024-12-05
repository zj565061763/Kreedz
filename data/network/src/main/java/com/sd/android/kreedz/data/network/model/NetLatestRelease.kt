package com.sd.android.kreedz.data.network.model

import com.sd.android.kreedz.data.network.http.moshi.RecordTime

data class NetLatestRelease(
  val newsId: String?,
  val newsName: String?,
  val demosByPlayer: List<NetLatestRecordGroup>,
)

data class NetLatestRecordGroup(
  val playerId: String,
  val playerName: String,
  val playerCountry: String?,
  val demos: List<NetLatestRecord>,
)

data class NetLatestRecord(
  val demoId: String,
  val mapId: String,
  val mapName: String,
  @RecordTime
  val time: Long = 0,
)