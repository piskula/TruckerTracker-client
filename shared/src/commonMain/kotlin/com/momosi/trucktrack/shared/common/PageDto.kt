package com.momosi.trucktrack.shared.common

import kotlinx.serialization.Serializable

@Serializable
data class PageDto<T>(
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int,
    val numberOfElements: Int,
    val content: List<T>,
)
