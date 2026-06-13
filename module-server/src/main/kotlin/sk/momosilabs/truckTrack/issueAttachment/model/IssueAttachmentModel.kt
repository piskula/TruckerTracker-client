package sk.momosilabs.truckTrack.issueAttachment.model

import sk.momosilabs.truckTrack.account.model.AccountModel
import java.time.OffsetDateTime

data class IssueAttachmentModel(
    val id: Long,
    val filename: String,
    val contentType: String,
    val sizeBytes: Long,
    val uploadedBy: AccountModel,
    val uploadedAt: OffsetDateTime,
)
