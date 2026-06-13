package sk.momosilabs.truckTrack.file.model

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.ResponseEntity
import java.nio.charset.StandardCharsets

fun TruckTrackFile.mapResponseEntity(): ResponseEntity<ByteArrayResource> =
    ResponseEntity.ok()
        .contentLength(content.size.toLong())
        .contentType(contentType)
        .header(CONTENT_DISPOSITION, filename.toContentDispositionUtf8())
        .body(ByteArrayResource(content))

private fun String.toContentDispositionUtf8() =
    ContentDisposition.attachment().filename(this, StandardCharsets.UTF_8).build().toString()
