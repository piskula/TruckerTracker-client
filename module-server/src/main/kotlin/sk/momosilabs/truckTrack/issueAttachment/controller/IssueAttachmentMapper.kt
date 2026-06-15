package sk.momosilabs.truckTrack.issueAttachment.controller

import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile
import sk.momosilabs.truckTrack.account.model.AccountModel
import sk.momosilabs.truckTrack.api.issue.dto.AccountDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueAttachmentDTO
import sk.momosilabs.truckTrack.file.model.TruckTrackFile
import sk.momosilabs.truckTrack.issueAttachment.model.IssueAttachmentModel

fun MultipartFile.toModel() = TruckTrackFile(
    filename = originalFilename ?: name,
    content = bytes,
    contentType = MediaType.parseMediaType(contentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE),
)

fun IssueAttachmentModel.toDTO() = IssueAttachmentDTO(
    id = id,
    filename = filename,
    contentType = contentType,
    sizeBytes = sizeBytes,
    uploadedBy = uploadedBy.toDTO(),
    uploadedAt = uploadedAt,
)

fun AccountModel.toDTO() = AccountDTO(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
)
