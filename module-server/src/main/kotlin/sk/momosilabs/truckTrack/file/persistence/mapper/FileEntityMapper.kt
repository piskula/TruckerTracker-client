package sk.momosilabs.truckTrack.file.persistence.mapper

import sk.momosilabs.truckTrack.account.persistence.mapper.toModel
import sk.momosilabs.truckTrack.file.entity.FileEntity
import sk.momosilabs.truckTrack.file.model.FileModel
import sk.momosilabs.truckTrack.util.toUtcOffsetDateTime

fun FileEntity.toModel() = FileModel(
    id = id,
    uuid = uuid,
    bucket = bucket,
    storageLocation = storageLocation,
    filename = filename,
    contentType = contentType,
    sizeBytes = sizeBytes,
    uploadedBy = uploadedBy.toModel(),
    uploadedAt = uploadedAtUtc.toUtcOffsetDateTime(),
)
