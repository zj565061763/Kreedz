package com.sd.android.kreedz.data.network.model

import com.sd.android.kreedz.data.network.http.moshi.RecordDate
import com.sd.android.kreedz.data.network.http.moshi.RecordTime

data class NetRecord(
  val mapId: String,
  val mapName: String,

  val demoId: String?,
  @RecordTime
  val time: Long = 0,
  @RecordDate
  val releaseDate: Long = 0,

  val playerId: String?,
  val playerName: String?,
  val country: String?,

  val youtubeLink: String?,
)