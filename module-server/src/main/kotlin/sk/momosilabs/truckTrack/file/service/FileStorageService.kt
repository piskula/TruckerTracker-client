package sk.momosilabs.truckTrack.file.service

import java.io.InputStream

interface FileStorageService {

    fun upload(inputStream: InputStream, bucket: String, key: String, contentType: String, sizeBytes: Long)

    fun download(bucket: String, key: String): InputStream

    fun delete(bucket: String, key: String)
}
