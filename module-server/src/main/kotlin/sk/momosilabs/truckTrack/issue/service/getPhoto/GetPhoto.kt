package sk.momosilabs.truckTrack.issue.service.getPhoto

import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.file.model.TruckTrackFile
import sk.momosilabs.truckTrack.file.service.FileStorageService
import sk.momosilabs.truckTrack.issue.service.IssueAttachmentPersistence

@Service
class GetPhoto(
    private val issueAttachmentPersistence: IssueAttachmentPersistence,
    private val fileStorageService: FileStorageService,
) : GetPhotoUseCase {

    @Transactional(readOnly = true)
    override fun get(attachmentId: Long): TruckTrackFile {
        val file = issueAttachmentPersistence.findFileById(attachmentId)
        val content = fileStorageService.download(file.bucket, file.storageLocation).readBytes()
        return TruckTrackFile(
            filename = file.filename,
            content = content,
            contentType = MediaType.parseMediaType(file.contentType),
        )
    }
}
