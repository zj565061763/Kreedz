package com.sd.android.kreedz.data.local

import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.lib.datastore.DatastoreType
import com.sd.lib.datastore.FDatastore
import com.sd.lib.datastore.get
import com.sd.lib.datastore.update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@DatastoreType("local_user_account")
internal data class LocalUserAccountModel(
  val id: String,
  val nickname: String,
  val country: String?,
  val countryName: String?,
  val icons: UserIconsModel,
  val roles: List<String>,
) {
  companion object {
    private val _store = FDatastore.get(LocalUserAccountModel::class.java)

    val flow: Flow<LocalUserAccountModel?>
      get() = _store.flow

    val userIdFlow: Flow<String?>
      get() = flow.map { it?.id }.distinctUntilChanged()

    suspend fun replace(model: LocalUserAccountModel?) {
      if (model?.id?.isBlank() == true) return
      _store.replace { model }
    }

    suspend fun update(model: LocalUserAccountModel) {
      _store.update { data ->
        if (data.id == model.id) {
          model
        } else {
          data
        }
      }
    }

    suspend fun get(): LocalUserAccountModel? {
      return _store.get()
    }
  }
}