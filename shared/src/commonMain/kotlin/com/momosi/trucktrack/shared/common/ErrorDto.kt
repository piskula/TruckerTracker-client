package com.momosi.trucktrack.shared.common

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class ErrorDto(
    val userMessage: String,
    val errorIdentifier: Uuid,
)
