package com.sd.android.kreedz.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.data.model.SearchNewsModel
import com.sd.android.kreedz.data.model.SearchUserModel
import com.sd.android.kreedz.feature.common.ui.ComEffect

@Composable
fun SearchScreen(
   modifier: Modifier = Modifier,
   vm: SearchVM = viewModel(),
   onClickBack: () -> Unit,
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val context = LocalContext.current

   val isReadyToSearch by remember { derivedStateOf { state.keyword.isNotEmpty() } }

   Scaffold(
      modifier = modifier,
      topBar = {
         SearchTitleView(
            textFieldState = vm.inputState,
            isReadyToSearch = isReadyToSearch,
            isSearching = state.isSearching,
            onClickBack = onClickBack,
            onClickSearch = {
               vm.clickSearch()
            },
         )
      },
   ) { padding ->
      BodyView(
         modifier = Modifier
            .fillMaxSize()
            .padding(padding),
         keyword = state.keyword,
         listNews = state.listNews,
         listUser = state.listUser,
         onClickUser = { user ->
            user.id?.also {
               AppRouter.user(context, it)
            }
         },
         onClickNews = { news ->
            AppRouter.news(context, news.id)
         },
      )
   }

   vm.effectFlow.ComEffect()
}

@Composable
private fun BodyView(
   modifier: Modifier = Modifier,
   keyword: String,
   listUser: List<SearchUserModel>?,
   listNews: List<SearchNewsModel>?,
   onClickUser: (SearchUserModel) -> Unit,
   onClickNews: (SearchNewsModel) -> Unit,
) {
   LazyColumn(
      modifier = modifier.fillMaxSize(),
      contentPadding = PaddingValues(
         start = 8.dp, end = 8.dp,
         top = 8.dp, bottom = 36.dp,
      ),
      verticalArrangement = Arrangement.spacedBy(8.dp),
   ) {
      searchResultUser(
         keyword = keyword,
         listUser = listUser,
         onClickUser = onClickUser,
      )

      searchResultNews(
         keyword = keyword,
         listNews = listNews,
         onClickNews = onClickNews,
      )
   }
}