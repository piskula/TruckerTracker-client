package sk.momosilabs.truckTrack.api.issue.dto

import java.time.OffsetDateTime

data class IssueAttachmentDto(
    val id: Long,
    val filename: String,
    val contentType: String,
    val sizeBytes: Long,
    val uploadedBy: AccountDto,
    val uploadedAt: OffsetDateTime,
)
