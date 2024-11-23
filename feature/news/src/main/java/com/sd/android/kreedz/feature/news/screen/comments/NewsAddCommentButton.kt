package com.sd.android.kreedz.feature.news.screen.comments

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
internal fun NewsAddCommentButton(
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
            contentDescription = "Add comment",
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