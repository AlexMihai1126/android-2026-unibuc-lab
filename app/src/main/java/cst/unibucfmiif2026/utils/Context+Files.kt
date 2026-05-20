package cst.unibucfmiif2026.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun Context.createImageUri(): Uri {
    val imageFile = File.createTempFile("user_avatar_", ".jpg", cacheDir)
    return FileProvider.getUriForFile(this, "$packageName.fileprovider", imageFile)
}
