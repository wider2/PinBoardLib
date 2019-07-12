package com.example.cachelibrary.CacheLib

import android.graphics.Bitmap

interface IPictureCache {

    fun get(url: String): Bitmap?

    fun put(url: String, bitmap: Bitmap)

    fun clear()
}