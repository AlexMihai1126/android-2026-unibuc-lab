package cst.unibucfmiif2026.utils

import android.app.Application
import android.net.Uri
import cst.unibucfmiif2026.ApplicationController
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun Uri.createImagePart(): MultipartBody.Part? {
    val contentResolver = ApplicationController.instance.contentResolver
    val mimeType = contentResolver.getType(this) ?: "image/jpeg"
    val imageBytes = contentResolver.openInputStream(this)?.use { inputStream ->
        inputStream.readBytes()
    } ?: return null
    val requestBody = imageBytes.toRequestBody(mimeType.toMediaType())

    return MultipartBody.Part.createFormData(
        name = "avatar",
        filename = contentResolver.getFileName(this),
        body = requestBody
    )
}