package sk.momosilabs.truckTrack.issue.persistence.mapper

import sk.momosilabs.truckTrack.account.persistence.mapper.toModel
import sk.momosilabs.truckTrack.issue.entity.IssueAttachmentEntity
import sk.momosilabs.truckTrack.issue.model.IssueAttachmentModel
import java.time.ZoneOffset

fun IssueAttachmentEntity.toModel() = IssueAttachmentModel(
    id = id,
    filename = file.filename,
    contentType = file.contentType,
    sizeBytes = file.sizeBytes,
    uploadedBy = file.uploadedBy.toModel(),
    uploadedAt = file.uploadedAtUtc.atOffset(ZoneOffset.UTC),
)
