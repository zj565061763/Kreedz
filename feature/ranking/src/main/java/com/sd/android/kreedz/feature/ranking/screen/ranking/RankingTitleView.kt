package com.sd.android.kreedz.feature.ranking.screen.ranking

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sd.android.kreedz.core.ui.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RankingTitleView(
   modifier: Modifier = Modifier,
   title: String,
   selectedDateStr: String,
   scrollBehavior: TopAppBarScrollBehavior?,
   onClickBack: () -> Unit,
   onClickDate: () -> Unit,
) {
   TopAppBar(
      modifier = modifier,
      colors = TopAppBarDefaults.topAppBarColors().let {
         it.copy(scrolledContainerColor = it.containerColor)
      },
      title = { Text(text = title) },
      navigationIcon = {
         IconButton(onClick = onClickBack) {
            Icon(
               imageVector = Icons.AutoMirrored.Filled.ArrowBack,
               contentDescription = "Back",
            )
         }
      },
      actions = {
         TextButton(
            onClick = onClickDate,
            shape = CircleShape,
            colors = ButtonDefaults.textButtonColors(
               contentColor = LocalContentColor.current,
            ),
         ) {
            if (selectedDateStr.isNotBlank()) {
               Text(text = selectedDateStr)
            } else {
               Icon(
                  imageVector = Icons.Default.DateRange,
                  contentDescription = "Select date",
               )
            }
         }
      },
      scrollBehavior = scrollBehavior,
   )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewTitleDefault() {
   AppTheme {
      RankingTitleView(
         title = "Title",
         selectedDateStr = "",
         scrollBehavior = null,
         onClickBack = {},
         onClickDate = { },
      )
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewTitleWithDate() {
   AppTheme {
      RankingTitleView(
         title = "Title",
         selectedDateStr = "2024-10-10",
         scrollBehavior = null,
         onClickBack = {},
         onClickDate = { },
      )
   }
}