package com.example.cachelibrary.CacheLib

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object PictureLoader {
    private var cache: IPictureCache? = null

    private var executorService: ExecutorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    private val uiHandler: Handler = Handler(Looper.getMainLooper())

    private lateinit var conn: HttpURLConnection

    const val TAG: String = "PictureLoader"


    fun setCache(cacheValue: IPictureCache) {
        if (cache == null) {
            cache = cacheValue
        }
    }

    fun displayImage(url: String, imageView: ImageView, placeholder: Int) {

        val cached = cache?.get(url)
        if (cached != null) {
            updateImageView(imageView, cached)
        } else {
            imageView.setImageResource(placeholder)
        }

        imageView.tag = url
        executorService.submit {
            val bitmap: Bitmap? = downloadImage(url)
            if (bitmap != null) {
                if (imageView.tag == url) {
                    updateImageView(imageView, bitmap)
                }
                cache?.put(url, bitmap)
            } else {
                Log.wtf(TAG, "Cache Download failed")
            }
        }
    }

    fun clearCache() {
        this.cache?.clear()
    }

    private fun updateImageView(imageView: ImageView, bitmap: Bitmap) {
        uiHandler.post {
            imageView.setImageBitmap(bitmap)
        }
    }

    fun pauseRequest() {
        try {
            if (::conn.isInitialized) conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resumeRequest(url: String, imageView: ImageView, placeholder: Int) {
        try {
            if (::conn.isInitialized) conn.connect()
            displayImage(url, imageView, placeholder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun downloadImage(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val url = URL(url)
            conn = url.openConnection() as HttpURLConnection
            conn.setConnectTimeout(10000)
            conn.readTimeout = 5000
            conn.useCaches = false
            bitmap = BitmapFactory.decodeStream(conn?.inputStream)
            conn.disconnect()
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, e.message)
        } catch (e: SocketException) {
            Log.d(TAG, e.message)
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return bitmap
    }
}