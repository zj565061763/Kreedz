package com.sd.android.kreedz.feature.news.screen.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.data.model.NewsCommentGroupModel
import com.sd.android.kreedz.data.model.NewsCommentModel
import com.sd.android.kreedz.data.model.NewsCommentReplyModel
import com.sd.android.kreedz.data.model.UserIconsModel
import com.sd.android.kreedz.data.model.UserWithIconsModel
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewSmall
import com.sd.android.kreedz.feature.common.ui.ComUserIconsView
import com.sd.android.kreedz.feature.common.ui.comAnnotatedLink
import com.sd.lib.compose.utils.fClick
import com.sd.lib.compose.utils.fEnabled

internal fun LazyListScope.newsCommentsView(
   comments: List<NewsCommentGroupModel>,
   onClickAuthor: (userId: String) -> Unit,
   onClickAddComment: () -> Unit,
   onClickComment: (NewsCommentModel) -> Unit,
) {
   item(
      key = "Comments",
      contentType = "Comments",
   ) {
      TitleView(
         onClickAddComment = onClickAddComment,
      )
   }

   comments.forEachIndexed { index, group ->
      item(
         key = group.comment.id,
         contentType = "group item",
      ) {
         GroupItemView(
            item = group.comment,
            onClickAuthor = {
               onClickAuthor(group.comment.author.id)
            },
            modifier = Modifier
               .fEnabled(index > 0) {
                  padding(top = 16.dp)
               }
               .fClick {
                  onClickComment(group.comment)
               }
         )
      }
      items(
         items = group.children,
         key = { it.comment.id },
         contentType = { "reply item" },
      ) { item ->
         val showReplyUser = item.reply != null
            && item.reply?.comment?.id != group.comment.id
         ReplyItemView(
            item = item,
            showReplyUser = showReplyUser,
            onClickAuthor = {
               onClickAuthor(item.comment.author.id)
            },
            modifier = Modifier
               .fClick {
                  onClickComment(item.comment)
               }
               .padding(start = 16.dp)
         )
      }
   }
}

@Composable
private fun TitleView(
   modifier: Modifier = Modifier,
   onClickAddComment: () -> Unit,
) {
   Row(
      modifier = modifier.fClick { onClickAddComment() },
      verticalAlignment = Alignment.CenterVertically,
   ) {
      Text(
         text = "Comments",
         fontSize = 16.sp,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.padding(vertical = 8.dp)
      )
      Spacer(Modifier.weight(1f))
      Icon(
         Icons.Filled.Add,
         contentDescription = "Add news comment",
      )
   }
}

@Composable
private fun GroupItemView(
   modifier: Modifier = Modifier,
   item: NewsCommentModel,
   onClickAuthor: () -> Unit,
) {
   ItemView(
      modifier = modifier,
      country = item.author.country,
      countryText = item.author.nickname,
      dateTime = item.dateTimeStr,
      message = item.message,
      icons = item.author.icons,
      onClickAuthor = onClickAuthor,
      replayUser = {},
   )
}

@Composable
private fun ReplyItemView(
   modifier: Modifier = Modifier,
   item: NewsCommentReplyModel,
   showReplyUser: Boolean,
   onClickAuthor: () -> Unit,
) {
   val comment = item.comment
   ItemView(
      modifier = modifier,
      country = comment.author.country,
      countryText = comment.author.nickname,
      dateTime = comment.dateTimeStr,
      message = comment.message,
      icons = comment.author.icons,
      onClickAuthor = onClickAuthor,
      replayUser = {
         if (showReplyUser) {
            item.reply?.comment?.let { comment ->
               ReplyUserView(
                  user = comment.author,
                  onClickAuthor = onClickAuthor,
               )
            }
         }
      },
   )
}

@Composable
private fun ReplyUserView(
   modifier: Modifier = Modifier,
   user: UserWithIconsModel,
   onClickAuthor: () -> Unit,
) {
   Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
   ) {
      Spacer(Modifier.width(4.dp))
      Icon(
         imageVector = Icons.Filled.PlayArrow,
         contentDescription = "Reply",
         modifier = Modifier.size(14.dp),
      )
      Spacer(Modifier.width(4.dp))
      ComCountryTextViewSmall(
         country = user.country,
         text = user.nickname,
         textColor = AppTextColor.small,
         modifier = Modifier.clickable {
            onClickAuthor()
         }
      )
   }
}

@Composable
private fun ItemView(
   modifier: Modifier = Modifier,
   country: String?,
   countryText: String?,
   dateTime: String,
   message: String,
   icons: UserIconsModel,
   onClickAuthor: () -> Unit,
   replayUser: @Composable () -> Unit,
) {
   ConstraintLayout(modifier = modifier.fillMaxWidth()) {
      val (
         refUser, refIcons, refReply,
         refDateTime, refMessage,
      ) = createRefs()

      ComCountryTextViewSmall(
         country = country,
         text = countryText,
         textColor = AppTextColor.small,
         modifier = Modifier
            .constrainAs(refUser) {
               top.linkTo(parent.top)
               start.linkTo(parent.start)
            }
            .clickable { onClickAuthor() }
      )

      ComUserIconsView(
         icons = icons,
         modifier = Modifier.constrainAs(refIcons) {
            centerVerticallyTo(refUser)
            start.linkTo(refUser.end, 2.dp)
         }
      )

      Box(modifier = Modifier.constrainAs(refReply) {
         centerVerticallyTo(refUser)
         start.linkTo(refIcons.end)
      }) {
         replayUser()
      }

      Text(
         text = dateTime,
         fontSize = 12.sp,
         color = AppTextColor.small,
         modifier = Modifier.constrainAs(refDateTime) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
         }
      )

      Box(
         modifier = Modifier.constrainAs(refMessage) {
            top.linkTo(refUser.bottom, 2.dp)
            start.linkTo(refUser.start)
         }
      ) {
         SelectionContainer {
            Text(
               text = message.comAnnotatedLink(),
               fontSize = 14.sp,
            )
         }
      }
   }
}

@Preview
@Composable
private fun PreviewTitleView() {
   TitleView(
      onClickAddComment = {},
   )
}

@Preview
@Composable
private fun PreviewItemView() {
   ItemView(
      country = "cn",
      countryText = "zhengjun",
      dateTime = "10/10/2024",
      message = "Welcome to kreedz https://www.youtube.com",
      icons = UserIconsModel(
         isVip = true,
         isRecordHolder = true,
         isLJRecordHolder = true,
         isTournamentRank1 = true,
         isTournamentRank2 = true,
         isTournamentRank3 = true,
         isMapper = true,
         isMovieEditor = true,
      ),
      onClickAuthor = {},
      replayUser = {},
   )
}