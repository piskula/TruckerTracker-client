package sk.momosilabs.truckTrack.file.storage

import io.minio.BucketExistsArgs
import io.minio.GetObjectArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import org.springframework.stereotype.Service
import sk.momosilabs.truckTrack.file.service.FileStorageService
import java.io.InputStream

@Service
class MinioFileStorageService(
    private val minioClient: MinioClient,
) : FileStorageService {

    override fun upload(inputStream: InputStream, bucket: String, key: String, contentType: String, sizeBytes: Long) {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
        }
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(key)
                .stream(inputStream, sizeBytes, -1)
                .contentType(contentType)
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
