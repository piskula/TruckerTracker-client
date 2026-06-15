package sk.momosilabs.truckTrack.issueAttachment.service.getPhoto

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.file.model.TruckTrackFile
import sk.momosilabs.truckTrack.file.service.FileStorageService
import sk.momosilabs.truckTrack.issueAttachment.service.IssueAttachmentPersistence
import sk.momosilabs.truckTrack.security.annotation.IsUser

@Service
class GetPhoto(
    private val issueAttachmentPersistence: IssueAttachmentPersistence,
    private val fileStorageService: FileStorageService,
) : GetPhotoUseCase {

    @IsUser
    @Transactional(readOnly = true)
    override fun get(attachmentId: Long): TruckTrackFile {
        val file = issueAttachmentPersistence.findFileById(attachmentId)
        val content = fileStorageService.download(file.bucket, file.storageLocation).readBytes()
        return TruckTrackFile(
            filename = file.filename,
            content = content,
            contentType = file.contentType,
        )
    }
}
