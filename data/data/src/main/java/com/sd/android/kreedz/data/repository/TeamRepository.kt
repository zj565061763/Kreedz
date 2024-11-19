package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.mapper.asUserIconsModel
import com.sd.android.kreedz.data.model.TeamRoleModel
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetTeamRole
import com.sd.android.kreedz.data.network.model.NetTeamUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun TeamRepository(): TeamRepository = TeamRepositoryImpl()

interface TeamRepository {
   suspend fun getTeam(): List<TeamRoleModel>
}

private class TeamRepositoryImpl : TeamRepository {
   private val _netDataSource = NetDataSource()

   override suspend fun getTeam(): List<TeamRoleModel> {
      val data = _netDataSource.getTeam()
      return withContext(Dispatchers.IO) {
         val groupedData = data.associateBy { it.role }
         TeamRole.entries
            .mapNotNull { groupedData[it.key] }
            .map { it.asTeamRoleModel() }
      }
   }
}

private enum class TeamRole(
   val key: String,
) {
   CEO("CEO"),
   COO("COO"),
   CTO("CTO"),

   HEAD_DEMO_CHECKER("HEAD_DEMO_CHECKER"),
   DEMO_CHECKER("DEMO_CHECKER"),

   HEAD_MAP_CHECKER("HEAD_MAP_CHECKER"),
   MAP_CHECKER("MAP_CHECKER"),

   DEVELOPER("DEVELOPER"),
   CUP_ADMIN("CUP_ADMIN"),
   MOVIE_EDITOR("MOVIE_EDITOR"),

   VIP("VIP"),
}

private fun NetTeamRole.asTeamRoleModel(): TeamRoleModel {
   return TeamRoleModel(
      role = role,
      roleName = role.replace("_", " "),
      users = users.map { it.asUserModel() }
   )
}

private fun NetTeamUser.asUserModel(): UserWithIconsModel {
   return UserWithIconsModel(
      id = id,
      nickname = pseudo,
      country = country,
      icons = icons?.asUserIconsModel() ?: UserIconsModel.Default,
   )
}