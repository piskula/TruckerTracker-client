package sk.momosilabs.truckTrack.api.issue

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import sk.momosilabs.truckTrack.api.common.PageDTO
import sk.momosilabs.truckTrack.api.common.PageableDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueAttachmentDTO

@Tag(name = "Issue Attachments")
interface IssueAttachmentApi {

    companion object {
        private const val ENDPOINT = "/api/v1/issue/{issueId}/photo"
    }

    @Operation(summary = "Get photo list")
    @GetMapping(ENDPOINT)
    fun getPhotoList(
        @PathVariable issueId: Long,
        @ParameterObject pageable: PageableDTO,
    ): PageDTO<IssueAttachmentDTO>

    @Operation(summary = "Upload photo")
    @PostMapping(ENDPOINT, consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadPhoto(
        @PathVariable issueId: Long,
        @RequestParam file: MultipartFile,
    ): IssueAttachmentDTO

    @Operation(summary = "Download photo")
    @GetMapping("$ENDPOINT/{attachmentId}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun downloadPhoto(
        @PathVariable issueId: Long,
        @PathVariable attachmentId: Long,
    ): ResponseEntity<ByteArrayResource>
}
