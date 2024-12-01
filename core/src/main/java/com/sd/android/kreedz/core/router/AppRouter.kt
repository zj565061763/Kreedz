package com.sd.android.kreedz.core.router

import android.content.Context
import com.didi.drouter.api.DRouter
import com.sd.lib.ctx.fFindActivity

object AppRouter {
   const val MAIN = "/main"
   const val PLAYER_RANKING = "/player_ranking"
   const val COUNTRY_RANKING = "/country_ranking"
   const val LJ_RECORDS = "/lj_records"

   const val MAP = "/map"
   const val USER = "/user"

   const val FAVORITE_MAPS = "/favorite_maps"

   const val NEWS = "/news"
   const val BLOG = "/blog"

   const val SEARCH = "/search"
   const val WEB = "/web"

   const val LOGIN = "/login"
   const val REGISTER = "/register"
   const val RECOVER_PASSWORD = "/recover_password"
   const val RECOVER_USERNAME = "/recover_username"

   fun main(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(MAIN).start(activity)
      }
   }

   fun playerRanking(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(PLAYER_RANKING).start(activity)
      }
   }

   fun countryRanking(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(COUNTRY_RANKING).start(activity)
      }
   }

   fun ljRecords(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(LJ_RECORDS).start(activity)
      }
   }

   fun map(context: Context, id: String) {
      if (id.isBlank()) return
      context.fFindActivity { activity ->
         DRouter.build(MAP)
            .putExtra("id", id)
            .start(activity)
      }
   }

   fun user(context: Context, id: String) {
      if (id.isBlank()) return
      context.fFindActivity { activity ->
         DRouter.build(USER)
            .putExtra("id", id)
            .start(activity)
      }
   }

   fun news(context: Context, id: String) {
      if (id.isBlank()) return
      context.fFindActivity { activity ->
         DRouter.build(NEWS)
            .putExtra("id", id)
            .start(activity)
      }
   }

   fun blog(context: Context, id: String) {
      if (id.isBlank()) return
      context.fFindActivity { activity ->
         DRouter.build(BLOG)
            .putExtra("id", id)
            .start(activity)
      }
   }

   fun login(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(LOGIN).start(activity)
      }
   }

   fun register(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(REGISTER).start(activity)
      }
   }

   fun recoverPassword(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(RECOVER_PASSWORD).start(activity)
      }
   }

   fun recoverUsername(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(RECOVER_USERNAME).start(activity)
      }
   }

   fun favoriteMaps(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(FAVORITE_MAPS).start(activity)
      }
   }

   fun web(context: Context, url: String) {
      if (url.isBlank()) return
      context.fFindActivity { activity ->
         DRouter.build(WEB)
            .putExtra("url", url)
            .start(activity)
      }
   }

   fun search(context: Context) {
      context.fFindActivity { activity ->
         DRouter.build(SEARCH).start(activity)
      }
   }
}