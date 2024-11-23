package com.sd.android.kreedz.feature.news.screen.news

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.data.repository.NewsRepository
import com.sd.lib.coroutines.FLoader

internal class NewsVM : BaseViewModel<NewsVM.State, Any>(State()) {
   private val _repository = NewsRepository()
   private val _loader = FLoader()

   fun load(id: String) {
      vmLaunch {
         if (id.isBlank()) return@vmLaunch
         if (state.id == id) return@vmLaunch

         _loader.cancelLoad()
         updateState { State() }

         _loader.load {
            loadData(id)
         }
      }
   }

   fun refresh() {
      vmLaunch {
         val id = state.id
         if (id.isBlank()) return@vmLaunch
         _loader.load {
            loadData(id)
         }
      }
   }

   private suspend fun loadData(id: String) {
      val data = _repository.getNews(id)
      with(data) {
         updateState {
            it.copy(
               id = id,
               title = title,
               author = author,
               dateStr = dateStr,
               htmlContent = htmlContent,
            )
         }
      }
   }

   init {
      vmLaunch {
         _loader.loadingFlow.collect { data ->
            updateState {
               it.copy(isLoading = data)
            }
         }
      }
   }

   @Immutable
   data class State(
      val isLoading: Boolean = false,

      val id: String = "",
      val title: String = "",
      val author: UserWithIconsModel? = null,
      val dateStr: String = "",
      val htmlContent: String = "",
   )
}