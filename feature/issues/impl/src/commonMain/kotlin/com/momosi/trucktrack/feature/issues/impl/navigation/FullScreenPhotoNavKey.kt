package com.momosi.trucktrack.feature.issues.impl.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
@Stable
sealed interface PhotoSource {
    @Serializable
    data class Url(val url: String) : PhotoSource

    @Serializable
    class Bytes(val bytes: ByteArray) : PhotoSource {
        override fun equals(other: Any?): Boolean = other is Bytes && bytes.contentEquals(other.bytes)
        override fun hashCode(): Int = bytes.contentHashCode()
    }
}

@Serializable
data class FullScreenPhotoNavKey(val source: PhotoSource) : NavKey
