package ru.ok.itmo.tamtam.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
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
        "#FF5733",
        "#33FF57",
        "#5733FF",
        "#FF5733",
        "#33FF57",
        "#5733FF",
        "#FF5733",
        "#33FF57",
        "#5733FF",
        "#FF5733",
        "#33FF57",
        "#5733FF",
        "#FF5733",
        "#33FF57",
        "#5733FF",
        "#FF5733",
        "#33FF57",
        "#5733FF",
        "#FF5733",
        "#33FF57",
        "#5733FF",
        "#FF5733",
        "#33FF57",
        "#5733FF",
        "#FF5733",
        "#33FF57",
        "#5733FF",
        "#FF5733",
        "#33FF57",
        "#5733FF"
    )

    fun getPathToAvatarForName(name: String): String {
        val targetPath = "$CACHE_DIR/name.png"
        val targetFile = File(context.cacheDir, targetPath)
        return if (targetFile.exists()) targetPath else generateAvatar(
            name = name
        )
    }

    private fun generateAvatar(
        name: String,
        avatarSize: Int = 120,
        textSize: Int = 26,
        backgroundColor: Int? = null
    ): String {
        val avatarBitmapDrawable =
            com.avatarfirst.avatargenlib.AvatarGenerator.AvatarBuilder(context)
                .setLabel(name)
                .setAvatarSize(avatarSize)
                .setTextSize(textSize)
                .toSquare()
                .toCircle()
                .setBackgroundColor(backgroundColor ?: Color.parseColor(colorsHex.random()))
                .build()

        return saveBitmapToDisk(avatarBitmapDrawable.bitmap, name)
    }

    private fun saveBitmapToDisk(bitmap: Bitmap, fileName: String): String {
        val directory = File(context.cacheDir, "$CACHE_DIR")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "$fileName.png")

        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "$CACHE_DIR/$fileName.png"
    }

    companion object {
        private const val CACHE_DIR = "image_cache"
    }
}