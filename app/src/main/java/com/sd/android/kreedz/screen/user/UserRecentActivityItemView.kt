package com.sd.android.kreedz.screen.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.data.model.UserRecentActivityModel
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
fun UserRecentActivityItemView(
   modifier: Modifier = Modifier,
   item: UserRecentActivityModel,
   onClickNewsComment: (newsId: String) -> Unit,
) {
   when {
      item.isChatBoxComment -> {
         ItemView(
            modifier = modifier,
            title = "comment",
            content = "ChatBox",
            date = item.activityDateStr,
         )
      }
      item.isNewsComment -> {
         ItemView(
            title = "comment",
            content = item.newsName ?: "",
            date = item.activityDateStr,
            modifier = modifier.clickable {
               item.newsId?.also { newsId ->
                  onClickNewsComment(newsId)
               }
            }
         )
      }
   }
}

@Composable
private fun ItemView(
   modifier: Modifier = Modifier,
   title: String,
   content: String,
   date: String,
) {
   ConstraintLayout(
      modifier = modifier
         .fillMaxWidth()
         .padding(9.dp)
   ) {
      val (refTitle, refComment, refDate) = createRefs()

      Text(
         text = title,
         fontSize = 14.sp,
         modifier = Modifier.constrainAs(refTitle) {
            linkTo(
               start = parent.start, end = parent.end,
               bias = 0f,
            )
         }
      )

      Text(
         text = content,
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier.constrainAs(refComment) {
            start.linkTo(parent.start)
            top.linkTo(refTitle.bottom, 6.dp)
            goneIf(content.isBlank())
         }
      )

      Text(
         text = date,
         fontSize = 12.sp,
         color = AppTextColor.medium,
         modifier = Modifier.constrainAs(refDate) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
         }
      )
   }
}

@Preview
@Composable
private fun Preview() {
   ItemView(
      title = "comment",
      content = "WR Relase #820 - 23 NEW WORLD RECORDS",
      date = "28/10/2024",
   )
}

@Preview
@Composable
private fun PreviewEmptyContent() {
   ItemView(
      title = "comment",
      content = "",
      date = "28/10/2024",
   )
}