package com.momosi.trucktrack.shared.common

import kotlinx.serialization.Serializable

@Serializable
data class PageableDto(
    val page: Int = 0,
    val size: Int = 20,
    val sort: String? = null,
)
