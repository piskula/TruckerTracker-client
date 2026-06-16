package com.momosi.trucktrack.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageDto<T>(
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val number: Int = 0,
    val size: Int = 0,
    val numberOfElements: Int = 0,
    val content: List<T> = emptyList(),
)

