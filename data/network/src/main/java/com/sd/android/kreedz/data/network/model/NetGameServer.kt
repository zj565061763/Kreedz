package com.sd.android.kreedz.data.network.model

data class NetGameServer(
   val name: String,
   val addr: String,
   val map: String,
   val players: String,
   val maxPlayers: String,

   val mapId: String?,
   val records: List<NetGameServerRecord>?,
)

data class NetGameServerRecord(
   val time: String,
   val playerName: String,
   val playerCountry: String?,
)