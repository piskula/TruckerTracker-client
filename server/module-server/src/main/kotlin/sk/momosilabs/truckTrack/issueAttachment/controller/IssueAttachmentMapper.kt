package sk.momosilabs.truckTrack.issueAttachment.controller

import com.momosi.trucktrack.shared.issue.AccountDto
import com.momosi.trucktrack.shared.issue.IssueAttachmentDto
import kotlin.time.toKotlinInstant
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile
import sk.momosilabs.truckTrack.account.model.AccountModel
import sk.momosilabs.truckTrack.file.model.TruckTrackFile
import sk.momosilabs.truckTrack.issueAttachment.model.IssueAttachmentModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

fun MultipartFile.toModel() = TruckTrackFile(
    filename = originalFilename ?: name,
    content = bytes,
    contentType = MediaType.parseMediaType(contentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE),
)

fun IssueAttachmentModel.toDto() = IssueAttachmentDto(
    id = id,
    filename = filename,
    contentType = contentType,
    sizeBytes = sizeBytes,
    uploadedBy = uploadedBy.toDto(),
    uploadedAt = uploadedAt.toInstant().toKotlinInstant(),
)

@OptIn(ExperimentalUuidApi::class)
fun AccountModel.toDto() = AccountDto(
    id = id.toKotlinUuid(),
    username = username,
    firstName = firstName,
    lastName = lastName,
)
