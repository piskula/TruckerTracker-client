package sk.momosilabs.truckTrack.file.model

import sk.momosilabs.truckTrack.account.model.AccountModel
import java.time.OffsetDateTime
import java.util.UUID

data class FileModel(
    val id: Long,
    val uuid: UUID,
    val bucket: String,
    val storageLocation: String,
    val filename: String,
    val contentType: String,
    val sizeBytes: Long,
    val uploadedBy: AccountModel,
    val uploadedAt: OffsetDateTime,
)
