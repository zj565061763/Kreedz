package com.sd.android.kreedz.data.repository

import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.sd.android.kreedz.data.repository.dao.DaoMapRepository
import com.sd.lib.coroutines.FKeyedState
import com.sd.lib.coroutines.FLoader
import com.sd.lib.coroutines.fGlobalLaunch
import com.sd.lib.coroutines.tryLoad
import com.sd.lib.ctx.fContext
import com.sd.lib.xlog.FLogger
import com.sd.lib.xlog.ld
import com.sd.lib.xlog.lw
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.yield

fun MapImageRepository(): MapImageRepository = MapImageRepositoryImpl

interface MapImageRepository {
   fun load(mapId: String, image: String?)
   fun loadingFlow(mapId: String): Flow<Boolean>
}

private object MapImageRepositoryImpl : MapImageRepository, FLogger {
   private val _daoMap = DaoMapRepository()
   private val _loaders: MutableMap<String, FLoader> = mutableMapOf()
   private val _loadingState = FKeyedState<Boolean>()

   override fun load(mapId: String, image: String?) {
      if (image.isNullOrBlank()) return
      fGlobalLaunch {
         val loader = _loaders.getOrPut(mapId) { FLoader() }
         loader.tryLoad(
            onFinish = {
               fGlobalLaunch {
                  _loadingState.emitAndRelease(mapId, state = false)
                  _loaders.remove(mapId)
               }
            }
         ) {
            _loadingState.emit(mapId, state = true)
            syncMapImage(mapId = mapId, image = image)
         }
      }
   }

   override fun loadingFlow(mapId: String): Flow<Boolean> {
      return _loadingState.flowOf(mapId)
   }

   private suspend fun syncMapImage(
      mapId: String,
      image: String,
   ) {
      _daoMap.getById(mapId).firstOrNull()?.also { map ->
         fContext.imageLoader.execute(
            ImageRequest.Builder(fContext)
               .data(map.image)
               .networkCachePolicy(CachePolicy.DISABLED)
               .build()
         ).also {
            if (it is SuccessResult) {
               return
            }
         }
      }

      yield()
      ld { "load image $image" }

      fContext.imageLoader.execute(
         ImageRequest.Builder(fContext)
            .data(image)
            .build()
      ).also {
         when (it) {
            is SuccessResult -> {
               ld { "load image success" }
               _daoMap.updateImage(id = mapId, image = image)
            }
            is ErrorResult -> {
               lw { "load image error ${it.throwable.stackTraceToString()}" }
               throw it.throwable
            }
         }
      }
   }
}