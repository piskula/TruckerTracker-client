package com.momosi.trucktrack.shared.issue

import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class IssueAttachmentDto(
    val id: Long,
    val filename: String,
    val contentType: String,
    val sizeBytes: Long,
    val uploadedBy: AccountDto,
    val uploadedAt: Instant,
)
