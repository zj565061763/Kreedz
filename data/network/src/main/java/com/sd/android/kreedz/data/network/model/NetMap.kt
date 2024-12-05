package com.sd.android.kreedz.data.network.model

import com.sd.android.kreedz.data.network.http.moshi.RecordDate
import com.sd.android.kreedz.data.network.http.moshi.RecordTime

class NetMap(
  val mapName: String,
  val mapImage: String?,
  val mapReleaseDate: String?,
  val currentRecord: NetMapRecord?,
  val authors: List<NetMapAuthor>?,
  val listRecords: List<NetMapRecord>?,
)

class NetMapAuthor(
  val mapperName: String,
  val mapAuthorId: String?,
  val mapperCountry: String?,
)

class NetMapRecord(
  val demoId: String,
  val mapId: String,
  val playerName: String,
  val playerId: String?,
  val country: String?,
  @RecordTime
  val time: Long = 0,
  @RecordDate
  val releaseDate: Long = 0,
  val isDeleted: Boolean,
  val youtubeLink: String?,
)