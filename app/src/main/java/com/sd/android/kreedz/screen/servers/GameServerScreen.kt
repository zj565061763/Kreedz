package com.sd.android.kreedz.screen.servers

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppPullToRefresh
import com.sd.android.kreedz.core.utils.AppUtils
import com.sd.android.kreedz.data.model.GameServerModel
import com.sd.android.kreedz.feature.common.ui.ComEffect

@Composable
fun GameServerScreen(
   modifier: Modifier = Modifier,
   vm: GameServerVM = viewModel(),
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val context = LocalContext.current

   AppPullToRefresh(
      modifier = modifier.fillMaxSize(),
      isRefreshing = state.isLoading,
      onRefresh = { vm.refresh() },
   ) {
      BodyView(
         servers = state.servers,
         onClickMap = { id ->
            AppRouter.map(context, id)
         },
         onClickAddress = { address ->
            if (AppUtils.copyText(address)) {
               Toast.makeText(context, "Server address copied", Toast.LENGTH_SHORT).show()
            }
         },
      )
   }

   LaunchedEffect(vm) {
      vm.init()
   }

   vm.effectFlow.ComEffect()
}

@Composable
private fun BodyView(
   modifier: Modifier = Modifier,
   servers: List<GameServerModel>,
   onClickMap: (String) -> Unit,
   onClickAddress: (String) -> Unit,
) {
   LazyVerticalStaggeredGrid(
      modifier = modifier.fillMaxSize(),
      columns = StaggeredGridCells.Fixed(2),
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalItemSpacing = 4.dp,
      contentPadding = PaddingValues(
         horizontal = 4.dp,
         vertical = 16.dp,
      ),
   ) {
      items(
         items = servers,
         key = { it.address },
      ) { item ->
         Card(shape = MaterialTheme.shapes.extraSmall) {
            GameServerItemView(
               title = item.name,
               map = item.map,
               mapImage = item.mapImage,
               address = item.address,
               players = item.players,
               maxPlayers = item.maxPlayers,
               playerName = item.record?.playerName,
               playerCountry = item.record?.playerCountry,
               time = item.record?.timeStr,
               onClickMap = {
                  item.mapId?.also { mapId ->
                     onClickMap(mapId)
                  }
               },
               onClickAddress = {
                  onClickAddress(item.address)
               },
            )
         }
      }
   }
}