package com.momosi.trucktrack.core.common.model

data class Page<T>(val totalElements: Long, val totalPages: Int, val number: Int, val size: Int, val numberOfElements: Int, val content: List<T>)
