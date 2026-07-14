package sk.momosilabs.truckTrack.issueAttachment.controller

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import sk.momosilabs.truckTrack.api.common.PageDTO
import sk.momosilabs.truckTrack.api.common.PageableDTO
import sk.momosilabs.truckTrack.api.issue.IssueAttachmentApi
import sk.momosilabs.truckTrack.api.issue.dto.IssueAttachmentDTO
import sk.momosilabs.truckTrack.file.model.mapResponseEntity
import sk.momosilabs.truckTrack.issueAttachment.service.getPhoto.GetPhotoUseCase
import sk.momosilabs.truckTrack.issueAttachment.service.getPhotoList.GetPhotoListUseCase
import sk.momosilabs.truckTrack.issueAttachment.service.uploadPhoto.UploadPhotoUseCase
import sk.momosilabs.truckTrack.util.toDto
import sk.momosilabs.truckTrack.util.toModel

@RestController
class IssueAttachmentController(
    private val getPhotoList: GetPhotoListUseCase,
    private val getPhoto: GetPhotoUseCase,
    private val uploadPhoto: UploadPhotoUseCase,
) : IssueAttachmentApi {

    override fun getPhotoList(issueId: Long, pageable: PageableDTO): PageDTO<IssueAttachmentDTO> =
        getPhotoList.list(issueId, pageable.toModel()).toDto { it.toDTO() }

    override fun uploadPhoto(issueId: Long, file: MultipartFile): IssueAttachmentDTO =
        uploadPhoto.upload(issueId = issueId, file = file.toModel()).toDTO()

    override fun downloadPhoto(issueId: Long, attachmentId: Long): ResponseEntity<ByteArrayResource> =
        getPhoto.get(attachmentId).mapResponseEntity()

}
