package com.sd.android.kreedz.screen.servers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sd.android.kreedz.core.ui.AppImage
import com.sd.android.kreedz.feature.common.ui.ComBlurCard
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewExtraSmall
import com.sd.android.kreedz.feature.more.R

@Composable
fun GameServerItemView(
  modifier: Modifier = Modifier,
  title: String,
  map: String,
  mapImage: Any,
  address: String,
  players: String,
  maxPlayers: String,
  time: String?,
  playerName: String?,
  playerCountry: String?,
  onClickMap: () -> Unit,
  onClickAddress: () -> Unit,
) {
  ConstraintLayout(
    modifier = modifier.fillMaxWidth()
  ) {
    val (refMapImage, refMapInfo, refServerInfo) = createRefs()

    // Map image
    AppImage(
      model = mapImage,
      contentDescription = map,
      contentScale = ContentScale.Crop,
      error = painterResource(R.drawable.map_no_image),
      modifier = Modifier
        .constrainAs(refMapImage) {
          width = Dimension.matchParent
          height = Dimension.ratio("1366:768")
          top.linkTo(parent.top)
        }
        .clickable { onClickMap() }
    )

    // Map info
    MapInfoView(
      map = map,
      players = players,
      maxPlayers = maxPlayers,
      playerName = playerName,
      playerCountry = playerCountry,
      time = time,
      modifier = Modifier.constrainAs(refMapInfo) {
        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints
        linkTo(
          start = refMapImage.start,
          end = refMapImage.end,
        )
        linkTo(
          top = refMapImage.top,
          bottom = refMapImage.bottom,
        )
      }
    )

    // Server info
    ServerInfoView(
      title = title,
      address = address,
      onClickAddress = onClickAddress,
      modifier = Modifier.constrainAs(refServerInfo) {
        width = Dimension.matchParent
        top.linkTo(refMapImage.bottom)
      }
    )
  }
}

@Composable
private fun MapInfoView(
  modifier: Modifier = Modifier,
  map: String,
  players: String,
  maxPlayers: String,
  time: String?,
  playerName: String?,
  playerCountry: String?,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(2.dp)
  ) {
    // Map record
    RecordInfoView(
      map = map,
      playerName = playerName,
      playerCountry = playerCountry,
      time = time,
      modifier = Modifier.align(Alignment.TopStart)
    )

    // Players
    PlayersInfoView(
      players = players,
      maxPlayers = maxPlayers,
      modifier = Modifier.align(Alignment.BottomEnd)
    )
  }
}

@Composable
private fun RecordInfoView(
  modifier: Modifier = Modifier,
  map: String,
  time: String?,
  playerName: String?,
  playerCountry: String?,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(2.dp),
  ) {
    // Map
    ComBlurCard {
      Text(
        text = map,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
      )
    }

    // Player
    if (!playerName.isNullOrBlank() || !playerCountry.isNullOrBlank()) {
      ComBlurCard {
        ComCountryTextViewExtraSmall(
          country = playerCountry,
          text = playerName,
        )
      }
    }

    // Time
    if (!time.isNullOrBlank()) {
      ComBlurCard {
        Text(
          text = time,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
        )
      }
    }
  }
}

@Composable
private fun PlayersInfoView(
  modifier: Modifier = Modifier,
  players: String,
  maxPlayers: String,
) {
  ComBlurCard(modifier = modifier) {
    Text(
      text = "${players}/${maxPlayers}",
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
    )
  }
}

@Composable
private fun ServerInfoView(
  modifier: Modifier = Modifier,
  title: String,
  address: String,
  onClickAddress: () -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(6.dp),
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClickAddress() }
      .padding(6.dp),
  ) {
    Text(
      text = title,
      fontSize = 12.sp,
    )
    Text(
      text = address,
      fontSize = 12.sp,
    )
  }
}

@Preview
@Composable
private fun PreviewItemView() {
  GameServerItemView(
    title = "KREEDZ.EE // Kreedz [Beginner/Easy]",
    map = "bkz_goldbhop",
    mapImage = R.drawable.bkz_goldbhop,
    address = "185.31.242.203:27002",
    players = "0",
    maxPlayers = "32",
    playerName = "topoviygus",
    playerCountry = "ru",
    time = "01:25.32",
    onClickMap = {},
    onClickAddress = {},
  )
}