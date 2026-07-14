package sk.momosilabs.truckTrack.file.storage

import io.minio.MinioClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(MinioProperties::class)
class MinioConfig {

    @Bean
    fun minioClient(props: MinioProperties): MinioClient =
        MinioClient.builder()
            .endpoint(props.url)
            .credentials(props.accessKey, props.secretKey)
            .build()
}
