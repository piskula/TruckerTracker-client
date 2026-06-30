package com.momosi.trucktrack.core.common.io

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoReaderImpl(private val context: Context) : PhotoReader {

    override suspend fun read(uri: String): PhotoData? = withContext(Dispatchers.IO) {
        runCatching {
            val androidUri = Uri.parse(uri)
            val mimeType = context.contentResolver.getType(androidUri) ?: "image/jpeg"
            val bytes = context.contentResolver.openInputStream(androidUri)?.use { it.readBytes() }
                ?: return@withContext null
            val fileName = "upload_${System.currentTimeMillis()}.jpg"
            PhotoData(bytes = bytes, fileName = fileName, mimeType = mimeType)
        }.getOrNull()
    }
}
