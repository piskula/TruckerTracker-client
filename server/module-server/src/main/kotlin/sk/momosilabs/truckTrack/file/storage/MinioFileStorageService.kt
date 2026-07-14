package sk.momosilabs.truckTrack.file.storage

import io.minio.BucketExistsArgs
import io.minio.GetObjectArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import org.springframework.stereotype.Service
import sk.momosilabs.truckTrack.file.model.TruckTrackFile
import sk.momosilabs.truckTrack.file.service.FileStorageService
import java.io.ByteArrayInputStream
import java.io.InputStream

@Service
class MinioFileStorageService(
    private val minioClient: MinioClient,
) : FileStorageService {

    override fun upload(file: TruckTrackFile, bucket: String, key: String) {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
        }
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(key)
                .stream(ByteArrayInputStream(file.content), file.content.size.toLong(), -1)
                .contentType(file.contentType.toString())
                .build()
        )
    }

    override fun download(bucket: String, key: String): InputStream =
        minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucket)
                .`object`(key)
                .build()
        )

    override fun delete(bucket: String, key: String) {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucket)
                .`object`(key)
                .build()
        )
    }
}
