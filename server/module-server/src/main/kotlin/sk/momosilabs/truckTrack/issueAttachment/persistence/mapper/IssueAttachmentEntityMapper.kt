package sk.momosilabs.truckTrack.issueAttachment.persistence.mapper

import sk.momosilabs.truckTrack.account.persistence.mapper.toModel
import sk.momosilabs.truckTrack.issueAttachment.entity.IssueAttachmentEntity
import sk.momosilabs.truckTrack.issueAttachment.model.IssueAttachmentModel
import sk.momosilabs.truckTrack.util.toUtcOffsetDateTime

fun IssueAttachmentEntity.toModel() = IssueAttachmentModel(
    id = id,
    filename = file.filename,
    contentType = file.contentType,
    sizeBytes = file.sizeBytes,
    uploadedBy = file.uploadedBy.toModel(),
    uploadedAt = file.uploadedAtUtc.toUtcOffsetDateTime(),
)
