package com.sd.android.kreedz.data.model

data class UserAccountModel(
   val id: String,
   val nickname: String,
   val country: String?,
   val countryName: String?,
   val icons: UserIconsModel,
   val roles: List<String>,
)