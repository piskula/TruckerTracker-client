package sk.momosilabs.truckTrack.api.common

import io.swagger.v3.oas.annotations.media.Schema

data class PageableDTO(
    @field:Schema(description = "Page number", defaultValue = "0")
    val page: Int = 0,
    @field:Schema(description = "Page size", defaultValue = "20")
    val size: Int = 20,
    @field:Schema(description = "Sort string (e.g. time,desc)")
    val sort: String? = null,
)
