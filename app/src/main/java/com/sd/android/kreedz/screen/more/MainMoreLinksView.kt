package com.sd.android.kreedz.screen.more

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.sd.android.kreedz.core.export.fsUri
import com.sd.android.kreedz.feature.more.R

@Composable
fun MainMoreLinksView(
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    SteamLinkView()
    DiscordLinkView()
    YoutubeLinkView()
    TwitchLinkView()
  }
}

@Composable
private fun SteamLinkView(
  modifier: Modifier = Modifier,
) {
  LinkView(
    modifier = modifier,
    icon = R.drawable.link_steam,
    link = "https://steamcommunity.com/groups/Xtreme-Jumps",
  )
}

@Composable
private fun DiscordLinkView(
  modifier: Modifier = Modifier,
) {
  LinkView(
    modifier = modifier,
    icon = R.drawable.link_discord,
    link = "https://discord.gg/AkggjC8PhF",
  )
}

@Composable
private fun YoutubeLinkView(
  modifier: Modifier = Modifier,
) {
  LinkView(
    modifier = modifier,
    icon = R.drawable.link_youtube,
    link = "https://www.youtube.com/XtremeJumps",
  )
}

@Composable
private fun TwitchLinkView(
  modifier: Modifier = Modifier,
) {
  LinkView(
    modifier = modifier,
    icon = R.drawable.link_twitch,
    link = "https://www.twitch.tv/xtremejumps",
  )
}

@Composable
private fun LinkView(
  modifier: Modifier = Modifier,
  @DrawableRes icon: Int,
  link: String,
) {
  val uriHandler = LocalUriHandler.current
  IconButton(
    modifier = modifier,
    onClick = {
      fsUri.openUri(link, uriHandler)
    },
  ) {
    Icon(
      painter = painterResource(icon),
      contentDescription = link,
    )
  }
}

@Preview
@Composable
private fun Preview() {
  MainMoreLinksView()
}