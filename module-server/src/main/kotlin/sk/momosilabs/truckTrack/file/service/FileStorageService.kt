package sk.momosilabs.truckTrack.file.service

import sk.momosilabs.truckTrack.file.model.TruckTrackFile
import java.io.InputStream

interface FileStorageService {

    fun upload(file: TruckTrackFile, bucket: String, key: String)

    fun download(bucket: String, key: String): InputStream

    fun delete(bucket: String, key: String)
}
