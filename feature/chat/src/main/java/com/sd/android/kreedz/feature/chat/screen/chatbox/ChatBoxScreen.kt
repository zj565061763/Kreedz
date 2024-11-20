package com.sd.android.kreedz.feature.chat.screen.chatbox

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.core.utils.AppUtils
import com.sd.android.kreedz.data.event.ReClickMainNavigation
import com.sd.android.kreedz.data.model.ChatBoxItemModel
import com.sd.android.kreedz.data.model.ChatBoxMessageModel
import com.sd.android.kreedz.data.model.MainNavigation
import com.sd.android.kreedz.feature.common.ui.ComEffect
import com.sd.android.kreedz.feature.common.ui.ComErrorView
import com.sd.android.kreedz.feature.common.ui.ComInputLayer
import com.sd.android.kreedz.feature.common.ui.ComLoadingDialog
import com.sd.lib.compose.paging.FUIStateRefresh
import com.sd.lib.compose.paging.fIsRefreshing
import com.sd.lib.event.FEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBoxScreen(
   modifier: Modifier = Modifier,
   vm: ChatBoxVM = viewModel(),
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val messages = vm.messages()

   val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
   val lazyListState = rememberLazyListState()
   val context = LocalContext.current

   var clickMessage by remember { mutableStateOf<ChatBoxMessageModel?>(null) }

   Scaffold(
      modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
      topBar = {
         TopAppBar(
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            expandedHeight = 48.dp,
            colors = TopAppBarDefaults.topAppBarColors().let {
               it.copy(scrolledContainerColor = it.containerColor)
            },
            title = {
               OnlineInfoView(
                  guestsCount = state.guestsCount,
                  onlineUsers = state.onlineUsers,
                  onClickOnlineUser = {
                     AppRouter.user(context, it)
                  },
               )
            }
         )
      },
      floatingActionButton = {
         AddMessageButton(
            visible = !lazyListState.isScrollInProgress,
            onClick = {
               vm.clickAdd(context)
            },
         )
      },
   ) { padding ->
      AppPullToRefresh(
         modifier = Modifier
            .fillMaxSize()
            .padding(padding),
         isRefreshing = messages.fIsRefreshing(),
         onRefresh = {
            messages.refresh()
         }
      ) {
         BodyView(
            lazyListState = lazyListState,
            messages = messages,
            onClickAuthor = {
               AppRouter.user(context, it)
            },
            onClickMessage = {
               clickMessage = it
            },
         )
      }
   }

   vm.effectFlow.ComEffect()

   LaunchedEffect(lazyListState, scrollBehavior) {
      FEvent.flowOf<ReClickMainNavigation>()
         .filter { it.navigation == MainNavigation.ChatBox }
         .collect {
            lazyListState.scrollToItem(0)
            scrollBehavior.state.heightOffset = 0f
         }
   }

   ComInputLayer(
      attach = state.showInput,
      onDetachRequest = {
         vm.closeInput()
      },
      inputState = vm.inputState,
      maxInput = state.maxInput,
      onClickSend = {
         vm.clickSend(context)
      },
   )

   ChatBoxMessageMenuLayer(
      attach = clickMessage != null,
      onDetachRequest = {
         clickMessage = null
      },
      onClickReply = {
         clickMessage?.also { model ->
            vm.clickReply(context, model)
            clickMessage = null
         }
      },
      onClickCopy = {
         clickMessage?.also { model ->
            AppUtils.copyText(model.message)
            clickMessage = null
         }
      },
      onClickDelete = if (
         state.userId.isNotBlank()
         && state.userId == clickMessage?.author?.id
      ) {
         {
            clickMessage?.also { model ->
               vm.clickDelete(context, model)
               clickMessage = null
            }
         }
      } else null,
   )

   if (state.isSending) {
      ComLoadingDialog {
         vm.cancelSend()
      }
   }

   if (state.isDeleting) {
      ComLoadingDialog {
         vm.cancelDelete()
      }
   }
}

@Composable
private fun BodyView(
   modifier: Modifier = Modifier,
   lazyListState: LazyListState,
   messages: LazyPagingItems<ChatBoxItemModel>,
   onClickAuthor: (userId: String) -> Unit,
   onClickMessage: (ChatBoxMessageModel) -> Unit,
) {
   Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
   ) {
      ChatBoxMessageListView(
         lazyListState = lazyListState,
         messages = messages,
         onClickAuthor = onClickAuthor,
         onClickMessage = onClickMessage,
      )
      messages.FUIStateRefresh(
         stateError = {
            ComErrorView(error = it)
         }
      )
   }
}

@Composable
private fun AddMessageButton(
   modifier: Modifier = Modifier,
   visible: Boolean,
   onClick: () -> Unit,
) {
   var visibleState by remember { mutableStateOf(visible) }
   AnimatedVisibility(
      modifier = modifier,
      visible = visibleState,
      enter = fadeIn(),
      exit = fadeOut(),
   ) {
      SmallFloatingActionButton(onClick = onClick) {
         Icon(
            Icons.Filled.Add,
            contentDescription = "Add chat box message",
         )
      }
   }

   if (visible) {
      LaunchedEffect(Unit) {
         delay(800)
         visibleState = true
      }
   } else {
      visibleState = false
   }
}