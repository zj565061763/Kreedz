package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class GameServerModel(
  val name: String,
  val address: String,
  val map: String,
  val mapImage: String,
  val players: String,
  val maxPlayers: String,
  val mapId: String?,
  val record: GameServerRecordModel?,
)

@Immutable
data class GameServerRecordModel(
  val timeStr: String,
  val playerName: String,
  val playerCountry: String?,
)