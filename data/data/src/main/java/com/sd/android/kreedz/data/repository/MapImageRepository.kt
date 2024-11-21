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
   fun getLoadingFlow(mapId: String): Flow<Boolean>
}

private object MapImageRepositoryImpl : MapImageRepository, FLogger {
   private val _daoMap = DaoMapRepository()
   private val _loaders: MutableMap<String, FLoader> = mutableMapOf()
   private val _loadingState = FKeyedState { false }

   override fun load(mapId: String, image: String?) {
      if (mapId.isBlank()) return
      if (image.isNullOrBlank()) return
      fGlobalLaunch {
         val loader = _loaders.getOrPut(mapId) { FLoader() }
         loader.tryLoad {
            try {
               _loadingState.update(mapId, state = true)
               loadImage(mapId = mapId, image = image)
            } finally {
               _loadingState.updateAndRelease(mapId, state = false)
               _loaders.remove(mapId)
            }
         }
      }
   }

   override fun getLoadingFlow(mapId: String): Flow<Boolean> {
      return _loadingState.flowOf(mapId)
   }

   private suspend fun loadImage(mapId: String, image: String) {
      // Check local cache first.
      _daoMap.getById(mapId).firstOrNull()?.also { map ->
         val result = fContext.imageLoader.execute(
            ImageRequest.Builder(fContext)
               .data(map.image)
               .networkCachePolicy(CachePolicy.DISABLED)
               .build()
         )
         if (result is SuccessResult) {
            return
         }
      }

      yield()
      ld { "load image $image" }

      // Load image from the network.
      val result = fContext.imageLoader.execute(
         ImageRequest.Builder(fContext)
            .data(image)
            .build()
      )

      when (result) {
         is SuccessResult -> {
            ld { "load image success" }
            // Save the image uri to local.
            _daoMap.updateImage(id = mapId, image = image)
         }
         is ErrorResult -> {
            lw { "load image error ${result.throwable.stackTraceToString()}" }
            throw result.throwable
         }
      }
   }
}