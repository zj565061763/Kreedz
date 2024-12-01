package com.sd.android.kreedz.feature.news.screen.news

import androidx.compose.runtime.Immutable
import com.sd.android.kreedz.core.base.BaseViewModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.data.repository.NewsRepository
import com.sd.lib.coroutines.FLoader

open class NewsVM : BaseViewModel<NewsVM.State, Any>(State()) {
   private val _repository by lazy { newsRepository() }
   private val _loader = FLoader()

   protected open fun newsRepository(): NewsRepository = NewsRepository()

   fun load(id: String) {
      vmLaunch {
         if (id.isBlank()) return@vmLaunch
         if (state.id == id) return@vmLaunch

         _loader.cancel()
         updateState { State(id = id) }
         loadData(id)
      }
   }

   fun refresh() {
      vmLaunch {
         val id = state.id
         if (id.isBlank()) return@vmLaunch
         loadData(id)
      }
   }

   private suspend fun loadData(id: String) {
      _loader.load {
         _repository.getNews(id)
      }.onSuccess { data ->
         with(data) {
            updateState {
               it.copy(
                  id = id,
                  title = title,
                  author = author,
                  dateStr = dateStr,
                  html = htmlContent,
               )
            }
         }
      }.onFailure { error ->
         sendEffect(error)
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
      val html: String = "",
   )
}