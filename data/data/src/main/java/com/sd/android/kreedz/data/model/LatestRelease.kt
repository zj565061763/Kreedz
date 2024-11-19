package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class LatestReleaseModel(
   val newsId: String?,
   val newsName: String?,
   val records: List<LatestRecordGroupModel>,
)

@Immutable
data class LatestRecordGroupModel(
   val player: UserModel,
   val records: List<LatestRecordModel>,
)

@Immutable
data class LatestRecordModel(
   val current: RecordModel,
   val previous: RecordModel?,
)