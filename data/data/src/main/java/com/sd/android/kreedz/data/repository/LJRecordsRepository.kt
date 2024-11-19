package com.sd.android.kreedz.data.repository

import com.sd.android.kreedz.data.model.GroupedLJRecordsModel
import com.sd.android.kreedz.data.model.LJRecordModel
import com.sd.android.kreedz.data.network.NetDataSource
import com.sd.android.kreedz.data.network.model.NetLJRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun LJRecordsRepository(): LJRecordsRepository = LJRecordsRepositoryImpl()

interface LJRecordsRepository {
   suspend fun getLJRecords(): List<GroupedLJRecordsModel>
}

private class LJRecordsRepositoryImpl : LJRecordsRepository {
   private val _netDataSource = NetDataSource()

   override suspend fun getLJRecords(): List<GroupedLJRecordsModel> {
      val data = _netDataSource.getLJRecords()
      return withContext(Dispatchers.IO) {
         val groupedData = data.associateBy { it.type }
         LJGroup.entries
            .mapNotNull { groupedData[it.key] }
            .map { data ->
               with(data) {
                  GroupedLJRecordsModel(
                     type = type,
                     records = records.map { it.asLJRecordModel() }
                  )
               }
            }
      }
   }
}

private enum class LJGroup(
   val key: String,
) {
   LongJump("LongJump"),
   CountJump("CountJump"),
   StandUpCountJump("StandUp CountJump"),
   MultiCountJump("Double/Multi CountJump"),
   HighJump("HighJump"),
   StandUpBhopJump("StandUp BhopJump"),
   BhopJump("BhopJump"),
   WeirdJump("WeirdJump"),
   LadderJump("LadderJump"),
   SlideLongJump("Slide LongJump"),
}

private fun NetLJRecord.asLJRecordModel(): LJRecordModel {
   return LJRecordModel(
      playerName = pseudo,
      playerCountry = country,
      block = block,
      distance = distance,
      prestrafe = prestrafe,
      topspeed = topspeed,
      youtubeLink = ytLink,
   )
}