package sk.momosilabs.truckTrack.api.common

data class PageableDto(
    val page: Int = 0,
    val size: Int = 20,
    val sort: String? = null,
)
