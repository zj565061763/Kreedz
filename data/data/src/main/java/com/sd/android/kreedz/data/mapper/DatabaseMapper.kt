package com.sd.android.kreedz.data.mapper

import com.sd.android.kreedz.data.database.entity.MapEntity
import com.sd.android.kreedz.data.database.entity.RecordEntity
import com.sd.android.kreedz.data.database.entity.UserEntity
import com.sd.android.kreedz.data.model.MapModel
import com.sd.android.kreedz.data.model.RecordModel
import com.sd.android.kreedz.data.model.UserModel
import com.sd.android.kreedz.data.utils.DataUtils

internal fun MapEntity.asMapModel(): MapModel {
  return MapModel(
    id = id,
    name = name,
    image = image,
  )
}

internal fun UserEntity.asUserModel(): UserModel {
  return UserModel(
    id = id,
    nickname = nickname,
    country = country,
  )
}

internal fun RecordEntity.asRecordModel(
  map: MapModel,
  player: UserModel,
): RecordModel {
  return RecordModel(
    id = id,
    map = map,
    player = player,
    time = time,
    date = date,
    youtubeLink = youtubeLink,
    deleted = deleted,
    timeStr = DataUtils.formatRecordTime(time),
    dateStr = DataUtils.formatDate(date),
  )
}