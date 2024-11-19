package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class NewsModel(
   val id: String,
   val title: String,
   val htmlContent: String,
   val dataStr: String,
   val author: UserWithIconsModel,
)

@Immutable
data class NewsCommentModel(
   val id: String,
   val message: String,
   val dateTimeStr: String,
   val author: UserWithIconsModel,
)

@Immutable
data class NewsCommentGroupModel(
   val comment: NewsCommentModel,
   val children: List<NewsCommentReplyModel>,
)

@Immutable
data class NewsCommentReplyModel(
   val comment: NewsCommentModel,
   val reply: NewsCommentReplyModel?,
)