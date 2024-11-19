package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class GroupedLJRecordsModel(
   val type: String,
   val records: List<LJRecordModel>,
)

@Immutable
data class LJRecordModel(
   val playerName: String,
   val playerCountry: String?,
   val block: String,
   val distance: String,
   val prestrafe: String,
   val topspeed: String,
   val youtubeLink: String?,
)