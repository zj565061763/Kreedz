package com.sd.android.kreedz.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class NewsModel(
   val id: String,
   val title: String,
   val htmlContent: String,
   val dateStr: String,
   val author: UserWithIconsModel,
)

@Immutable
data class NewsCommentListModel(
   val count: Int,
   val groups: List<NewsCommentGroupModel>,
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

@Immutable
data class NewsCommentModel(
   val id: String,
   val comment: String,
   val dateStr: String,
   val author: UserWithIconsModel,
)