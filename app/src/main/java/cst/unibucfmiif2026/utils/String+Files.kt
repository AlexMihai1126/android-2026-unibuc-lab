package cst.unibucfmiif2026.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun String.toPlainTextRequestBody(): RequestBody =
    toRequestBody("text/plain".toMediaType())