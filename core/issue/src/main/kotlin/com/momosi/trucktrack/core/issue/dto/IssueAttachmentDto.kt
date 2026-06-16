package com.momosi.trucktrack.core.issue.dto

import kotlinx.serialization.Serializable

@Serializable
data class IssueAttachmentDto(
    val id: Long = 0,
    val filename: String = "",
    val contentType: String = "",
    val sizeBytes: Long = 0,
    val uploadedBy: AccountDto? = null,
    val uploadedAt: String = "",
)

