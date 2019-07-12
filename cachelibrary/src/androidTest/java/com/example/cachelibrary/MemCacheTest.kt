package com.example.cachelibrary

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.cachelibrary.CacheLib.InitialCache
import com.example.cachelibrary.CacheLib.MemoryCache
import com.example.cachelibrary.CacheLib.PictureLoader
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

@RunWith(AndroidJUnit4::class)
class MemCacheTest {

    private val url: String = "https://banners.adfox.ru/181203/adfox/877909/2778712.jpg";
    private val memCache = MemoryCache()

    @Before
    fun setUp() {
        PictureLoader.setCache(InitialCache())
    }

    @After
    fun tearDown() {
        PictureLoader.clearCache()
    }

    @Test
    fun checkRealBitmap() {

        val remoteBitmap : Bitmap? = PictureLoader.downloadImage(url)

        memCache.put(url, remoteBitmap!!)

        val bitmap = memCache.get(url)

        assertNotNull(bitmap)
        assertEquals(remoteBitmap, bitmap)
    }


    @Test
    fun testSaveBitmapToTempDirectoryWithSuccess() {
        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        val file = saveBitmapToTempDirectory(
            BitmapFactory.decodeResource(application.resources, R.drawable.profile35),
            "bitmap",
            Bitmap.CompressFormat.PNG, application
        )
        Assert.assertTrue(file.absolutePath.endsWith("bitmap.png"))
    }

    fun saveBitmapToTempDirectory(bitmap: Bitmap, filename: String, format: Bitmap.CompressFormat, application:Application): File {
        val tempDirectory = File(application.cacheDir, "images").apply { mkdirs() }
        val file = File(tempDirectory, "$filename.${format.name.toLowerCase()}")
        FileOutputStream(file).use { outStream ->
            bitmap.compress(format, 100, outStream)
        }
        return file
    }

}
