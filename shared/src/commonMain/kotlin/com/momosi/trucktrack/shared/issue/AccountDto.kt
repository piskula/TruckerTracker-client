package com.momosi.trucktrack.shared.issue

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class AccountDto(
    val id: Uuid,
    val username: String,
    val firstName: String,
    val lastName: String,
)
