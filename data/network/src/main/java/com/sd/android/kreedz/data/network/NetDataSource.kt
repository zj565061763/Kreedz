package com.sd.android.kreedz.data.network

import com.sd.android.kreedz.data.network.http.AppApi
import com.sd.android.kreedz.data.network.model.NetBlog
import com.sd.android.kreedz.data.network.model.NetChatBoxMessage
import com.sd.android.kreedz.data.network.model.NetCountryRanking
import com.sd.android.kreedz.data.network.model.NetGameServer
import com.sd.android.kreedz.data.network.model.NetGroupedLJRecords
import com.sd.android.kreedz.data.network.model.NetLatestBlog
import com.sd.android.kreedz.data.network.model.NetLatestNews
import com.sd.android.kreedz.data.network.model.NetLatestRelease
import com.sd.android.kreedz.data.network.model.NetLogin
import com.sd.android.kreedz.data.network.model.NetMap
import com.sd.android.kreedz.data.network.model.NetNews
import com.sd.android.kreedz.data.network.model.NetNewsComment
import com.sd.android.kreedz.data.network.model.NetPlayerRanking
import com.sd.android.kreedz.data.network.model.NetRecord
import com.sd.android.kreedz.data.network.model.NetSearch
import com.sd.android.kreedz.data.network.model.NetTeamRole
import com.sd.android.kreedz.data.network.model.NetTopRanking
import com.sd.android.kreedz.data.network.model.NetUserProfile
import com.sd.android.kreedz.data.network.model.NetUserRecordStats
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

fun NetDataSource(): NetDataSource = NetDataSourceImpl

interface NetDataSource {
   suspend fun getRecords(page: Int): List<NetRecord>
   suspend fun getLJRecords(): List<NetGroupedLJRecords>

   suspend fun getTopRanking(): NetTopRanking
   suspend fun getPlayerRanking(date: String?): List<NetPlayerRanking>
   suspend fun getCountryRanking(date: String?): List<NetCountryRanking>

   suspend fun getLatestRelease(): NetLatestRelease

   suspend fun getMap(id: String): NetMap

   suspend fun getUserProfile(id: String): NetUserProfile
   suspend fun getUserRecordsStats(id: String): NetUserRecordStats

   suspend fun getLatestNews(page: Int): NetLatestNews
   suspend fun getNews(newsId: String): NetNews
   suspend fun newsComments(newsId: String): List<NetNewsComment>
   suspend fun newsSendComment(newsId: String, content: String, replyCommentId: String?)
   suspend fun newsDeleteComment(id: String)

   suspend fun getLatestBlog(page: Int): NetLatestBlog
   suspend fun getBlog(blogId: String): NetBlog
   suspend fun blogComments(blogId: String): List<NetNewsComment>
   suspend fun blogSendComment(blogId: String, content: String, replyCommentId: String?)
   suspend fun blogDeleteComment(id: String)

   suspend fun getGameServers(): List<NetGameServer>
   suspend fun getTeam(): List<NetTeamRole>

   suspend fun login(username: String, password: String): NetLogin
   suspend fun register(email: String, nickname: String, username: String, password: String)
   suspend fun logout()

   suspend fun recoverPassword(email: String)
   suspend fun recoverUsername(email: String)

   suspend fun chatBoxMessages(page: Int): List<NetChatBoxMessage>
   suspend fun chatBoxSendMessage(content: String)
   suspend fun chatBoxDeleteMessage(id: String)

   suspend fun search(keyword: String): NetSearch
}

private object NetDataSourceImpl : NetDataSource {
   private val _api = ModuleNetwork.createApi(AppNetApi::class.java)

   override suspend fun getRecords(page: Int): List<NetRecord> {
      return _api.getRecords(page)
   }

   override suspend fun getLJRecords(): List<NetGroupedLJRecords> {
      return _api.getLJRecords()
   }

   override suspend fun getTopRanking(): NetTopRanking {
      return _api.getTopRanking()
   }

   override suspend fun getPlayerRanking(date: String?): List<NetPlayerRanking> {
      return if (date == null) {
         _api.getPlayerRanking()
      } else {
         _api.getPlayerRankingInDate(date)
      }
   }

   override suspend fun getCountryRanking(date: String?): List<NetCountryRanking> {
      if (date == null) {
         return _api.getCountryRanking()
      } else {
         return _api.getCountryRankingInDate(date)
      }
   }

   override suspend fun getLatestRelease(): NetLatestRelease {
      return _api.getLatestRelease()
   }

   override suspend fun getMap(id: String): NetMap {
      return _api.getMap(id)
   }

   override suspend fun getUserProfile(id: String): NetUserProfile {
      return _api.getUserProfile(id)
   }

   override suspend fun getUserRecordsStats(id: String): NetUserRecordStats {
      return _api.getUserRecordStats(id)
   }

   override suspend fun getLatestNews(page: Int): NetLatestNews {
      return _api.getLatestNews(page)
   }

   override suspend fun getNews(newsId: String): NetNews {
      return _api.getNews(newsId)
   }

   override suspend fun newsComments(newsId: String): List<NetNewsComment> {
      return _api.newsComments(newsId)
   }

   override suspend fun newsSendComment(newsId: String, content: String, replyCommentId: String?) {
      _api.newsSendComment(newsId, content, replyCommentId)
   }

   override suspend fun newsDeleteComment(id: String) {
      _api.newsDeleteComment(id)
   }

   override suspend fun getLatestBlog(page: Int): NetLatestBlog {
      return _api.getLatestBlog(page)
   }

   override suspend fun getBlog(blogId: String): NetBlog {
      return _api.getBlog(blogId)
   }

   override suspend fun blogComments(blogId: String): List<NetNewsComment> {
      return _api.blogComments(blogId)
   }

   override suspend fun blogSendComment(blogId: String, content: String, replyCommentId: String?) {
      _api.blogSendComment(blogId, content, replyCommentId)
   }

   override suspend fun blogDeleteComment(id: String) {
      _api.blogDeleteComment(id)
   }

   override suspend fun getGameServers(): List<NetGameServer> {
      return _api.getGameServers()
   }

   override suspend fun getTeam(): List<NetTeamRole> {
      return _api.getTeam()
   }

   override suspend fun login(username: String, password: String): NetLogin {
      return _api.login(username = username, password = password)
   }

   override suspend fun register(email: String, nickname: String, username: String, password: String) {
      _api.register(
         email = email,
         nickname = nickname,
         username = username,
         password = password,
      )
   }

   override suspend fun logout() {
      _api.logout()
   }

   override suspend fun recoverPassword(email: String) {
      _api.recoverPassword(email)
   }

   override suspend fun recoverUsername(email: String) {
      _api.recoverUsername(email)
   }

   override suspend fun chatBoxMessages(page: Int): List<NetChatBoxMessage> {
      return _api.getChatBoxMessages(page)
   }

   override suspend fun chatBoxSendMessage(content: String) {
      _api.chatBoxSendMessage(content)
   }

   override suspend fun chatBoxDeleteMessage(id: String) {
      _api.chatBoxDeleteMessage(id)
   }

   override suspend fun search(keyword: String): NetSearch {
      return _api.search(keyword = keyword, types = "user,news")
   }
}

private interface AppNetApi {
   @AppApi
   @GET("record/all-demos")
   suspend fun getRecords(
      @Query("page") page: Int,
      @Query("sortColumn") sortColumn: String = "mapName",
      @Query("sortDirection") sortDirection: String = "ASC",
   ): List<NetRecord>

   @AppApi
   @GET("longjump/top")
   suspend fun getLJRecords(): List<NetGroupedLJRecords>

   @AppApi
   @GET("record/top5")
   suspend fun getTopRanking(): NetTopRanking

   @AppApi
   @GET("record/player-demos")
   suspend fun getPlayerRanking(): List<NetPlayerRanking>

   @AppApi
   @GET("record/player-demos")
   suspend fun getPlayerRankingInDate(
      @Query("date") date: String,
   ): List<NetPlayerRanking>

   @AppApi
   @GET("record/country-demos")
   suspend fun getCountryRanking(): List<NetCountryRanking>

   @AppApi
   @GET("record/country-demos")
   suspend fun getCountryRankingInDate(
      @Query("date") date: String,
   ): List<NetCountryRanking>

   @AppApi
   @GET("record/latest-demos")
   suspend fun getLatestRelease(): NetLatestRelease


   @AppApi
   @GET("record/map")
   suspend fun getMap(
      @Query("mapId") mapId: String,
   ): NetMap

   @AppApi
   @GET("user/profile")
   suspend fun getUserProfile(
      @Query("userId") userId: String,
   ): NetUserProfile

   @AppApi
   @GET("user/stats")
   suspend fun getUserRecordStats(
      @Query("userId") userId: String,
   ): NetUserRecordStats

   @AppApi(resultLog = false)
   @GET("news/last-news")
   suspend fun getLatestNews(
      @Query("page") page: Int,
   ): NetLatestNews

   @AppApi
   @GET("news/news")
   suspend fun getNews(
      @Query("newsId") newsId: String,
   ): NetNews

   @AppApi
   @GET("news/comments")
   suspend fun newsComments(
      @Query("newsId") newsId: String,
   ): List<NetNewsComment>

   @AppApi
   @FormUrlEncoded
   @POST("news/comment")
   suspend fun newsSendComment(
      @Query("newsId") newsId: String,
      @Field("commentMessage") content: String,
      @Field("parentId") replyCommentId: String?,
   )

   @AppApi
   @DELETE("news/comment")
   suspend fun newsDeleteComment(
      @Query("commentId") commentId: String,
   )

   @AppApi(resultLog = false)
   @GET("article/last-articles")
   suspend fun getLatestBlog(
      @Query("page") page: Int,
   ): NetLatestBlog

   @AppApi
   @GET("article/article")
   suspend fun getBlog(
      @Query("articleId") blogId: String,
   ): NetBlog

   @AppApi
   @GET("article/comments")
   suspend fun blogComments(
      @Query("articleId") blogId: String,
   ): List<NetNewsComment>

   @AppApi
   @FormUrlEncoded
   @POST("article/comment")
   suspend fun blogSendComment(
      @Query("articleId") blogId: String,
      @Field("commentMessage") content: String,
      @Field("parentId") replyCommentId: String?,
   )

   @AppApi
   @DELETE("article/comment")
   suspend fun blogDeleteComment(
      @Query("commentId") commentId: String,
   )

   @AppApi
   @GET("game-server/list")
   suspend fun getGameServers(): List<NetGameServer>

   @AppApi
   @GET("user/admin-team")
   suspend fun getTeam(): List<NetTeamRole>

   @AppApi
   @FormUrlEncoded
   @POST("auth/authenticate")
   suspend fun login(
      @Field("username") username: String,
      @Field("password") password: String,
   ): NetLogin

   @AppApi
   @FormUrlEncoded
   @POST("auth/register")
   suspend fun register(
      @Field("email") email: String,
      @Field("pseudo") nickname: String,
      @Field("username") username: String,
      @Field("password") password: String,
   )

   @AppApi
   @POST("auth/logout")
   suspend fun logout()

   @AppApi
   @GET("user/password-reset")
   suspend fun recoverPassword(
      @Query("userEmail") email: String,
   )

   @AppApi
   @GET("user/username-recovery")
   suspend fun recoverUsername(
      @Query("userEmail") email: String,
   )

   @AppApi
   @GET("chatbox/comments")
   suspend fun getChatBoxMessages(
      @Query("page") page: Int,
   ): List<NetChatBoxMessage>

   @AppApi
   @FormUrlEncoded
   @POST("chatbox/comment")
   suspend fun chatBoxSendMessage(
      @Field("commentMessage") content: String,
   )

   @AppApi
   @DELETE("chatbox/comment")
   suspend fun chatBoxDeleteMessage(
      @Query("commentId") id: String,
   )

   @AppApi
   @GET("search/")
   suspend fun search(
      @Query("expression") keyword: String,
      @Query("types") types: String,
   ): NetSearch
}