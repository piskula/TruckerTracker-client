package com.momosi.trucktrack.core.network.model

data class Page<T>(
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int,
    val numberOfElements: Int,
    val content: List<T>,
)

