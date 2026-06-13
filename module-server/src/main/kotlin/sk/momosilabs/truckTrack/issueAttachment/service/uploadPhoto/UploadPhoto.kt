package sk.momosilabs.truckTrack.issueAttachment.service.uploadPhoto

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.file.model.FileModel
import sk.momosilabs.truckTrack.file.service.FilePersistence
import sk.momosilabs.truckTrack.file.service.FileStorageService
import sk.momosilabs.truckTrack.issueAttachment.model.IssueAttachmentModel
import sk.momosilabs.truckTrack.issueAttachment.service.IssueAttachmentPersistence
import sk.momosilabs.truckTrack.security.CurrentUserService
import sk.momosilabs.truckTrack.security.annotation.IsUser
import java.io.ByteArrayInputStream
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class UploadPhoto(
    private val issueAttachmentPersistence: IssueAttachmentPersistence,
    private val filePersistence: FilePersistence,
    private val fileStorageService: FileStorageService,
    private val currentUserService: CurrentUserService,
) : UploadPhotoUseCase {

    @IsUser
    @Transactional
    override fun upload(command: UploadPhotoCommand): IssueAttachmentModel {
        val currentUser = currentUserService.currentUser()
        val uuid = UUID.randomUUID()
        val bucket = "issue-photos"

        fileStorageService.upload(
            inputStream = ByteArrayInputStream(command.file.content),
            bucket = bucket,
            key = uuid.toString(),
            contentType = command.file.contentType.toString(),
            sizeBytes = command.file.content.size.toLong(),
        )

        val savedFile = filePersistence.create(
            FileModel(
                id = 0L,
                uuid = uuid,
                bucket = bucket,
                storageLocation = uuid.toString(),
                filename = command.file.filename,
                contentType = command.file.contentType.toString(),
                sizeBytes = command.file.content.size.toLong(),
                uploadedBy = currentUser,
                uploadedAt = OffsetDateTime.now(ZoneOffset.UTC),
            )
        )

        return issueAttachmentPersistence.create(command.issueId, savedFile)
    }
}
