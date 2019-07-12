package com.example.cachelibrary.CacheLib

import android.graphics.Bitmap
import android.util.LruCache

class MemoryCache : IPictureCache {
    private val cache: LruCache<String, Bitmap>
    init {
        val maxMemory: Long = Runtime.getRuntime().maxMemory() / 1024

        val cacheSize: Int = (maxMemory / 1.2).toInt()

        cache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, bitmap: Bitmap?): Int {
                return (bitmap?.rowBytes ?: 0) * (bitmap?.height ?: 0) / 1024
            }
        }
    }

    override fun get(url: String): Bitmap? {
        return cache.get(url)
    }

    override fun put(url: String, bitmap: Bitmap) {
        cache.put(url, bitmap)
    }

    override fun clear() {
        cache.evictAll()
    }

}