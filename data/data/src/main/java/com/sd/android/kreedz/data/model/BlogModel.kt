package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class BlogModel(
   val id: String,
   val title: String,
   val htmlContent: String,
   val dataStr: String,
   val author: UserWithIconsModel,
)