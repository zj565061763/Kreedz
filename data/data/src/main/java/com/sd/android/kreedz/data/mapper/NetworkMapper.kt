package com.sd.android.kreedz.data.mapper

import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.network.model.NetIcons

internal fun NetIcons.asUserIconsModel(): UserIconsModel {
   return UserIconsModel(
      isVip = isVip,
      isRecordHolder = isRecordHolder,
      isLJRecordHolder = isLjRecordHolder,
      isTournamentRank1 = isTournamentRank1,
      isTournamentRank2 = isTournamentRank2,
      isTournamentRank3 = isTournamentRank3,
      isMapper = isMapper,
      isMovieEditor = isMovieMaker,
   )
}