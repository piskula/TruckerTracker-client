package sk.momosilabs.truckTrack.file.model

import org.springframework.http.MediaType
import java.util.Objects

data class TruckTrackFile(
    val filename: String,
    val content: ByteArray,
    val contentType: MediaType,
) {

    override fun equals(other: Any?): Boolean = this === other ||
            other is TruckTrackFile && this.filename == other.filename && this.contentType == other.contentType

    override fun hashCode(): Int = Objects.hash(filename, contentType)

}
