package com.sd.android.kreedz.screen.user

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sd.android.kreedz.core.router.AppRouter
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.data.model.UserAchievementModel
import com.sd.android.kreedz.data.model.UserRecentActivityModel
import com.sd.android.kreedz.data.model.UserRecentRecordModel
import com.sd.android.kreedz.data.model.UserRecordModel
import com.sd.android.kreedz.data.model.UserRecordStatsModel
import com.sd.android.kreedz.feature.common.ui.ComErrorView
import com.sd.android.kreedz.feature.common.ui.ComResultBox
import com.sd.android.kreedz.screen.user.records.UserRecordsView
import com.sd.lib.compose.utils.fEnabled

@Composable
fun UserScreen(
   modifier: Modifier = Modifier,
   userId: String,
   vm: UserVM = viewModel(),
   onClickBack: () -> Unit,
) {
   val state by vm.stateFlow.collectAsStateWithLifecycle()
   val context = LocalContext.current

   var showRecords by remember { mutableStateOf(false) }

   Scaffold(
      modifier = modifier,
      topBar = {
         UserTitleView(
            nickname = state.nickname,
            onClickBack = onClickBack,
         )
      }
   ) { padding ->
      ComResultBox(
         modifier = Modifier
            .fillMaxSize()
            .padding(padding),
         isLoading = state.isLoading,
         result = state.result,
         onFailure = {
            ComErrorView(
               error = it,
               onClickRetry = { vm.retry() }
            )
         },
      ) {
         BodyView(
            lastVisit = state.lastVisit,
            country = state.country,
            countryName = state.countryName,
            regDate = state.regDate,
            roles = state.roles,
            siteVisits = state.siteVisits,
            chatBoxComments = state.chatBoxComments,

            isLoadingRecordsStats = state.isLoadingRecordsStats,
            recordsStats = state.recordsStats,
            onClickRecord = {
               it.mapId?.also { mapId ->
                  AppRouter.map(context, mapId)
               }
            },
            onClickMoreRecords = {
               showRecords = true
            },

            recentActivity = state.recentActivity,
            recentRecords = state.recentRecords,
            achievements = state.achievements,
            onClickRecentActivityNewsComment = {
               AppRouter.news(context, it)
            },
            onClickRecentRecords = {
               AppRouter.map(context, it)
            },
            onClickRecentRecordsNews = {
               AppRouter.news(context, it)
            },
            onClickAchievement = {
               AppRouter.news(context, it)
            },
         )
      }
   }

   LaunchedEffect(vm, userId) {
      vm.load(userId)
   }

   if (showRecords) {
      UserRecordsView(
         onDismissRequest = { showRecords = false },
         currentRecords = state.recordsStats.currentRecords ?: emptyList(),
         previousRecords = state.recordsStats.previousRecords ?: emptyList(),
         onClickItem = {
            AppRouter.map(context, it)
         },
      )
   }
}

@Composable
private fun BodyView(
   modifier: Modifier = Modifier,
   lastVisit: String,
   country: String?,
   countryName: String?,
   regDate: String,
   roles: List<String>,
   siteVisits: String,
   chatBoxComments: String,

   isLoadingRecordsStats: Boolean,
   recordsStats: UserRecordStatsModel?,
   onClickRecord: (UserRecordModel) -> Unit,
   onClickMoreRecords: () -> Unit,

   recentActivity: List<UserRecentActivityModel>,
   recentRecords: List<UserRecentRecordModel>,
   achievements: List<UserAchievementModel>,
   onClickRecentActivityNewsComment: (newsId: String) -> Unit,
   onClickRecentRecords: (mapId: String) -> Unit,
   onClickRecentRecordsNews: (newsId: String) -> Unit,
   onClickAchievement: (newsId: String) -> Unit,
) {
   LazyColumn(
      modifier = modifier.fillMaxSize(),
      contentPadding = PaddingValues(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
   ) {
      userLastVisit(lastVisit = lastVisit)

      userInfo(
         country = country,
         countryName = countryName,
         regDate = regDate,
         roles = roles,
         siteVisits = siteVisits,
         chatBoxComments = chatBoxComments,
      )

      recordsStats?.run {
         userRecordsStats(
            isLoadingRecordsStats = isLoadingRecordsStats,
            rank = rank,
            numberOfCurrentRecords = numberCurrentRecords,
            numberOfTotalRecords = numberTotalRecords,
            numberOfMaps = numberDifferentMaps,
            firstRecord = firstRecord,
            lastRecord = lastRecord,
            onClickRecord = onClickRecord,
            onClickMoreRecords = onClickMoreRecords,
         )
      }

      userRecentActivity(
         recentActivity = recentActivity,
         onClickNewsComment = onClickRecentActivityNewsComment,
      )

      userRecentRecords(
         recentRecords = recentRecords,
         onClickNews = onClickRecentRecordsNews,
         onClickRecord = onClickRecentRecords,
      )

      userAchievements(
         achievements = achievements,
         onClickAchievement = onClickAchievement,
      )
   }
}

private fun LazyListScope.userLastVisit(
   lastVisit: String,
) {
   if (lastVisit.isBlank()) return
   item(
      key = "last visit",
      contentType = "last visit",
   ) {
      Box(
         contentAlignment = Alignment.Center,
         modifier = Modifier.fillParentMaxWidth()
      ) {
         Text(
            text = "Last visit $lastVisit",
            fontSize = 12.sp,
            color = AppTextColor.medium,
         )
      }
   }
}

private fun LazyListScope.userInfo(
   country: String?,
   countryName: String?,
   regDate: String,
   roles: List<String>,
   siteVisits: String,
   chatBoxComments: String,
) {
   item(
      key = "info",
      contentType = "info",
   ) {
      Card(shape = MaterialTheme.shapes.extraSmall) {
         UserInfoView(
            country = country,
            countryName = countryName,
            regDate = regDate,
            roles = roles,
            siteVisits = siteVisits,
            chatBoxComments = chatBoxComments,
         )
      }
   }
}

private fun LazyListScope.userRecordsStats(
   isLoadingRecordsStats: Boolean,
   rank: String?,
   numberOfCurrentRecords: String?,
   numberOfTotalRecords: String?,
   numberOfMaps: String?,
   firstRecord: UserRecordModel?,
   lastRecord: UserRecordModel?,
   onClickRecord: (UserRecordModel) -> Unit,
   onClickMoreRecords: () -> Unit,
) {
   item(
      key = "records stats",
      contentType = "records stats",
   ) {
      Card(shape = MaterialTheme.shapes.extraSmall) {
         UserRecordsStatsView(
            isLoadingRecordsStats = isLoadingRecordsStats,
            rank = rank,
            numberOfCurrentRecords = numberOfCurrentRecords,
            numberOfTotalRecords = numberOfTotalRecords,
            numberOfMaps = numberOfMaps,
            firstRecord = firstRecord,
            lastRecord = lastRecord,
            onClickRecord = onClickRecord,
            modifier = Modifier.fEnabled(firstRecord != null) {
               clickable { onClickMoreRecords() }
            }
         )
      }
   }
}

private fun LazyListScope.userRecentActivity(
   recentActivity: List<UserRecentActivityModel>,
   onClickNewsComment: (newsId: String) -> Unit,
) {
   if (recentActivity.isEmpty()) return
   sectionTitle(title = "RECENT ACTIVITY")
   sectionItems(
      items = recentActivity,
      contentType = { "activity" },
   ) { item ->
      UserRecentActivityItemView(
         item = item,
         onClickNewsComment = onClickNewsComment,
      )
   }
}

private fun LazyListScope.userRecentRecords(
   recentRecords: List<UserRecentRecordModel>,
   onClickNews: (newsId: String) -> Unit,
   onClickRecord: (mapId: String) -> Unit,
) {
   if (recentRecords.isEmpty()) return
   sectionTitle(title = "RECENT RECORDS")
   sectionItems(
      items = recentRecords,
      contentType = { "record" },
   ) { item ->
      UserRecentRecordsItemView(
         map = item.mapName,
         time = item.timeStr,
         currentRecord = item.currentRecord,
         date = item.dateStr,
         news = item.newsName,
         onClickNews = {
            item.newsId?.also { newsId ->
               onClickNews(newsId)
            }
         },
         modifier = Modifier.clickable {
            item.mapId?.also { mapId ->
               onClickRecord(mapId)
            }
         },
      )
   }
}

private fun LazyListScope.userAchievements(
   achievements: List<UserAchievementModel>,
   onClickAchievement: (newsId: String) -> Unit,
) {
   if (achievements.isEmpty()) return
   sectionTitle(title = "ACHIEVEMENTS")
   sectionItems(
      items = achievements,
      contentType = { "achievement" },
   ) { item ->
      UserAchievementsItemView(
         title = item.title,
         rank = item.rank,
         date = item.dateStr,
         modifier = Modifier.clickable {
            item.newsId?.also { newsId ->
               onClickAchievement(newsId)
            }
         }
      )
   }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.sectionTitle(title: String) {
   stickyHeader(
      key = "$title title",
      contentType = "title",
   ) {
      Box(
         modifier = Modifier
            .fillParentMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 4.dp, vertical = 6.dp),
         contentAlignment = Alignment.CenterStart,
      ) {
         Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
         )
      }
   }
}

private fun <T> LazyListScope.sectionItems(
   items: List<T>,
   contentType: (item: T) -> Any?,
   itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) {
   items(
      items = items,
      contentType = contentType,
   ) { item ->
      Card(shape = MaterialTheme.shapes.extraSmall) {
         itemContent(item)
      }
   }
}