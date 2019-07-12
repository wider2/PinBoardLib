package com.example.cachelibrary.CacheLib

import android.graphics.Bitmap

class InitialCache() : IPictureCache {

    private val memCache = MemoryCache()

    override fun get(url: String): Bitmap? {
        return memCache.get(url)
    }

    override fun put(url: String, bitmap: Bitmap) {
        memCache.put(url, bitmap)
    }

    override fun clear() {
        memCache.clear()
    }
}