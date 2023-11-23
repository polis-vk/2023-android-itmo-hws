package ru.ok.itmo.tamtam.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Base64
import ru.ok.itmo.tamtam.ioc.scope.AppComponentScope
import ru.ok.itmo.tamtam.utils.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@AppComponentScope
class AvatarGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val colorsHex = listOf(
        "#bc6c25",
        "#283618",
        "#606c38",
        "#2a9d8f",
        "#f4a261",
        "#a2d2ff",
        "#003566",
        "#ffd60a",
        "#780000",
        "#8338ec",
        "#06d6a0",
        "#ff70a6",
    )

    fun getPathToAvatarForName(name: String): String {
        val encodedBytes = Base64.encode(name.toByteArray(), Base64.DEFAULT)
        val encodedName = String(encodedBytes, Charsets.UTF_8)

        val targetPath = "$CACHE_DIR/$encodedName.png"
        val targetFile = File(context.cacheDir, targetPath)

        if (!targetFile.exists()) generateAvatar(
            name = name,
            file = targetFile
        )
        return targetPath
    }

    private fun generateAvatar(
        name: String,
        file: File,
        avatarSize: Int = 120,
        textSize: Int = 26,
        backgroundColor: Int? = null
    ) {
        val avatarBitmapDrawable =
            com.avatarfirst.avatargenlib.AvatarGenerator.AvatarBuilder(context)
                .setLabel(name)
                .setAvatarSize(avatarSize)
                .setTextSize(textSize)
                .toSquare()
                .toCircle()
                .setBackgroundColor(backgroundColor ?: Color.parseColor(colorsHex.random()))
                .build()

        saveBitmapToDisk(avatarBitmapDrawable.bitmap, file)
    }

    private fun saveBitmapToDisk(bitmap: Bitmap, file: File) {
        val directory = File(context.cacheDir, "$CACHE_DIR")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun clearImageCache() {
        val cacheDir = File(context.cacheDir, CACHE_DIR)
        if (cacheDir.exists() && cacheDir.isDirectory) {
            cacheDir.deleteRecursively()
        }
    }

    companion object {
        private const val CACHE_DIR = "image_cache"
    }
}