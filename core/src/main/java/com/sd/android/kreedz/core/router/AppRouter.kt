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
   const val FAVORITE_USERS = "/favorite_users"

   const val NEWS = "/news"
   const val BLOG = "/blog"

   const val WEB = "/web"

   const val LOGIN = "/login"
   const val REGISTER = "/register"
   const val RECOVER_PASSWORD = "/recover_password"
   const val RECOVER_USERNAME = "/recover_username"

   fun main(context: Context) {
      context.fFindActivity {
         DRouter.build(MAIN).start(it)
      }
   }

   fun playerRanking(context: Context) {
      context.fFindActivity {
         DRouter.build(PLAYER_RANKING).start(it)
      }
   }

   fun countryRanking(context: Context) {
      context.fFindActivity {
         DRouter.build(COUNTRY_RANKING).start()
      }
   }

   fun ljRecords(context: Context) {
      context.fFindActivity {
         DRouter.build(LJ_RECORDS).start(it)
      }
   }

   fun map(context: Context, id: String) {
      if (id.isBlank()) return
      context.fFindActivity {
         DRouter.build(MAP)
            .putExtra("id", id)
            .start(it)
      }
   }

   fun user(context: Context, id: String) {
      if (id.isBlank()) return
      context.fFindActivity {
         DRouter.build(USER)
            .putExtra("id", id)
            .start(it)
      }
   }

   fun news(context: Context, id: String) {
      if (id.isBlank()) return
      context.fFindActivity {
         DRouter.build(NEWS)
            .putExtra("id", id)
            .start(it)
      }
   }

   fun blog(context: Context, id: String) {
      if (id.isBlank()) return
      context.fFindActivity {
         DRouter.build(BLOG)
            .putExtra("id", id)
            .start(it)
      }
   }

   fun web(context: Context, url: String) {
      if (url.isBlank()) return
      context.fFindActivity {
         DRouter.build(WEB)
            .putExtra("url", url)
            .start(it)
      }
   }

   fun login(context: Context) {
      context.fFindActivity {
         DRouter.build(LOGIN).start()
      }
   }

   fun register(context: Context) {
      context.fFindActivity {
         DRouter.build(REGISTER).start()
      }
   }

   fun recoverPassword(context: Context) {
      context.fFindActivity {
         DRouter.build(RECOVER_PASSWORD).start()
      }
   }

   fun recoverUsername(context: Context) {
      context.fFindActivity {
         DRouter.build(RECOVER_USERNAME).start()
      }
   }

   fun favoriteMaps(context: Context) {
      context.fFindActivity {
         DRouter.build(FAVORITE_MAPS).start()
      }
   }

   fun favoriteUsers(context: Context) {
      context.fFindActivity {
         DRouter.build(FAVORITE_USERS).start()
      }
   }
}