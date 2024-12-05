package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class UserRecordStatsModel(
  val rank: String?,
  val numberCurrentRecords: String?,
  val numberTotalRecords: String?,
  val numberDifferentMaps: String?,
  val firstRecord: UserRecordModel?,
  val lastRecord: UserRecordModel?,
  val currentRecords: List<UserRecordModel>?,
  val previousRecords: List<UserRecordModel>?,
)

@Immutable
data class UserRecordModel(
  val mapName: String,
  val timeStr: String,
  val dateStr: String,
  val mapId: String?,
  val timeDifferenceStr: String?,
)