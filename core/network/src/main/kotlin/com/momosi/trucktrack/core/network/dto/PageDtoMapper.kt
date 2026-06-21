package com.momosi.trucktrack.core.network.dto

import com.momosi.trucktrack.core.common.model.Page

fun <T, R> PageDto<T>.toPage(mapper: (T) -> R): Page<R> = Page(
    totalElements = totalElements,
    totalPages = totalPages,
    number = number,
    size = size,
    numberOfElements = numberOfElements,
    content = content.map(mapper),
)
