package com.sd.android.kreedz.screen.more

import com.sd.android.kreedz.BuildConfig
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.repository.AccountRepository
import com.sd.lib.coroutines.FLoader

class MainMoreVM : BaseViewModel<MainMoreVM.State, Any>(State()) {
  private val _accountRepository = AccountRepository()
  private val _dataLoader = FLoader()

  fun refresh() {
    vmLaunch {
      _dataLoader.load {
        loadData()
      }.onFailure { error ->
        sendEffect(error)
      }
    }
  }

  fun logout() {
    vmLaunch {
      _accountRepository.logout()
    }
  }

  private suspend fun loadData() {
    _accountRepository.syncAccount()
  }

  init {
    vmLaunch {
      _dataLoader.loadingFlow.collect { data ->
        updateState {
          it.copy(isLoading = data)
        }
      }
    }

    vmLaunch {
      _accountRepository.getUserAccountFlow()
        .collect { data ->
          updateState {
            it.copy(
              userId = data?.id,
              nickname = data?.nickname,
              country = data?.country,
              countryName = data?.countryName,
              icons = data?.icons ?: UserIconsModel.Default,
              roles = data?.roles ?: emptyList(),
            )
          }
        }
    }
  }

  data class State(
    val isLoading: Boolean = false,
    val userId: String? = null,
    val nickname: String? = null,
    val country: String? = null,
    val countryName: String? = null,
    val roles: List<String> = emptyList(),
    val icons: UserIconsModel = UserIconsModel.Default,
    val versionName: String = BuildConfig.VERSION_NAME,
  )
}