package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.model.GameServerModel
import com.sd.android.kreedz.data.model.GameServerRecordModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetGameServer
import com.sd.android.kreedz.data.network.model.NetGameServerRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun GameServerRepository(): GameServerRepository = GameServerRepositoryImpl()

interface GameServerRepository {
   suspend fun getGameServers(): List<GameServerModel>
}

private class GameServerRepositoryImpl : GameServerRepository {
   private val _netDataSource = NetDataSource()

   override suspend fun getGameServers(): List<GameServerModel> {
      val data = _netDataSource.getGameServers()
      return withContext(Dispatchers.IO) {
         data.asSequence()
            .map { it.asGameServerModel() }
            .filter { it.map.isNotBlank() }
            .filter { it.address.isNotBlank() }
            .distinctBy { it.address }
            .toList()
      }
   }
}

private fun NetGameServer.asGameServerModel(): GameServerModel {
   return GameServerModel(
      name = name,
      address = addr,
      map = map,
      mapImage = "https://kz-rush.ru/xr_images/maps/cs16/$map.jpg",
      players = players,
      maxPlayers = maxPlayers,
      mapId = mapId,
      record = records?.firstOrNull()?.asGameServerRecordModel(),
   )
}

private fun NetGameServerRecord.asGameServerRecordModel(): GameServerRecordModel {
   return GameServerRecordModel(
      playerName = playerName,
      playerCountry = playerCountry,
      timeStr = time,
   )
}