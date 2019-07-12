package com.example.cachelibrary

import com.example.cachelibrary.CacheApi.CacheApiModel
import com.example.cachelibrary.CacheLib.InitialCache
import com.example.cachelibrary.CacheLib.PictureLoader

class CacheLibrary {

    fun init() {
        PictureLoader.setCache(InitialCache())
    }

    fun clear() {
        PictureLoader.clearCache()
    }

    fun load(cacheApiModel: CacheApiModel) {
        try {
            PictureLoader.displayImage(cacheApiModel.url, cacheApiModel.imageView, cacheApiModel.placeholder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resumeRequest(cacheApiModel: CacheApiModel) {
        PictureLoader.resumeRequest(cacheApiModel.url, cacheApiModel.imageView, cacheApiModel.placeholder)
    }

    fun pauseRequest() {
        PictureLoader.pauseRequest()
    }
}