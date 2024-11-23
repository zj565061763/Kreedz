package com.sd.android.kreedz.feature.news.screen.comments

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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

internal fun LazyListScope.newsCommentsView(
   isLoadingComments: Boolean,
   commentCount: Int?,
   comments: List<NewsCommentGroupModel>?,
   onClickAuthor: (userId: String) -> Unit,
   onClickComment: (NewsCommentModel) -> Unit,
) {
   item(
      key = "comment:title",
      contentType = "comment:title",
   ) {
      TitleView(
         commentCount = commentCount,
         isLoadingComments = isLoadingComments,
      )
   }

   comments?.forEachIndexed { index, group ->
      if (index > 0) {
         item(contentType = "comment:divider") {
            HorizontalDivider(thickness = Dp.Hairline)
         }
      }
      item(
         key = "comment:${group.comment.id}",
         contentType = "comment:group item",
      ) {
         GroupItemView(
            item = group.comment,
            onClickAuthor = {
               onClickAuthor(group.comment.author.id)
            },
            modifier = Modifier.fClick {
               onClickComment(group.comment)
            }
         )
      }
      items(
         items = group.children,
         key = { "comment:${it.comment.id}" },
         contentType = { "comment:reply item" },
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
   commentCount: Int?,
   isLoadingComments: Boolean,
) {
   Row(
      modifier = modifier.animateContentSize(),
      verticalAlignment = Alignment.CenterVertically,
   ) {
      Text(
         text = "Comments",
         fontSize = 16.sp,
         fontWeight = FontWeight.Medium,
         modifier = Modifier.padding(vertical = 16.dp)
      )

      Spacer(Modifier.width(4.dp))

      if (isLoadingComments) {
         CircularProgressIndicator(
            strokeWidth = 1.dp,
            modifier = Modifier.size(14.dp),
         )
      } else {
         if (commentCount != null) {
            Text(
               text = "($commentCount)",
               fontSize = 16.sp,
               fontWeight = FontWeight.Medium,
               modifier = Modifier.padding(vertical = 16.dp)
            )
         }
      }
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
      dateStr = item.dateStr,
      comment = item.comment,
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
      dateStr = comment.dateStr,
      comment = comment.comment,
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
private fun ItemView(
   modifier: Modifier = Modifier,
   country: String?,
   countryText: String?,
   dateStr: String,
   comment: String,
   icons: UserIconsModel,
   onClickAuthor: () -> Unit,
   replayUser: @Composable () -> Unit,
) {
   ConstraintLayout(
      modifier = modifier
         .fillMaxWidth()
         .padding(vertical = 4.dp)
   ) {
      val (
         refUser, refIcons, refReply,
         refDateTime, refComment,
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

      // Reply
      Box(modifier = Modifier.constrainAs(refReply) {
         centerVerticallyTo(refUser)
         start.linkTo(refIcons.end)
      }) {
         replayUser()
      }

      // Comment
      Box(
         modifier = Modifier.constrainAs(refComment) {
            top.linkTo(refUser.bottom, 1.dp)
            start.linkTo(refUser.start, 16.dp)
            linkTo(
               start = refUser.start, end = parent.end,
               startMargin = 16.dp, endMargin = 16.dp,
               bias = 0f,
            )
            width = Dimension.fillToConstraints
         }
      ) {
         SelectionContainer {
            Text(
               text = comment.comAnnotatedLink(),
               fontSize = 14.sp,
               lineHeight = 18.sp,
            )
         }
      }

      // Date
      Text(
         text = dateStr,
         fontSize = 11.sp,
         color = AppTextColor.small,
         modifier = Modifier.constrainAs(refDateTime) {
            top.linkTo(refComment.bottom, 1.dp)
            start.linkTo(refComment.start)
         }
      )
   }
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

      Spacer(Modifier.width(2.dp))
      ComUserIconsView(icons = user.icons)
   }
}

@Preview
@Composable
private fun PreviewTitleView() {
   TitleView(
      commentCount = 33,
      isLoadingComments = false,
   )
}

@Preview
@Composable
private fun PreviewTitleViewLoading() {
   TitleView(
      commentCount = 33,
      isLoadingComments = true,
   )
}

@Preview
@Composable
private fun PreviewReplyUserView() {
   ReplyUserView(
      user = UserWithIconsModel(
         id = "",
         nickname = "zhengjun",
         country = "cn",
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
      ),
      onClickAuthor = {},
   )
}

@Preview
@Composable
private fun PreviewItemView() {
   ItemView(
      country = "cn",
      countryText = "zhengjun",
      dateStr = "10/10/2024",
      comment = "Welcome to kreedz Welcome to kreedz Welcome to kreedz Welcome to kreedz",
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