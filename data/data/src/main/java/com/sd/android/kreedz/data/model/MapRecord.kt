package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class MapRecordModel(
  val map: MapModel,
  val record: RecordModel?,
)

@Immutable
data class MapModel(
  val id: String,
  val name: String,
  val image: String?,
)

@Immutable
data class RecordModel(
  val id: String,
  val map: MapModel,
  val player: UserModel,
  val time: Long,
  val date: Long,
  val youtubeLink: String?,
  val deleted: Boolean,

  val timeStr: String,
  val dateStr: String,
)