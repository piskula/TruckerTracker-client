package sk.momosilabs.truckTrack.issueAttachment.service.uploadPhoto

import sk.momosilabs.truckTrack.file.model.TruckTrackFile
import sk.momosilabs.truckTrack.issueAttachment.model.IssueAttachmentModel

data class UploadPhotoCommand(
    val issueId: Long,
    val file: TruckTrackFile,
)

interface UploadPhotoUseCase {
    fun upload(command: UploadPhotoCommand): IssueAttachmentModel
}
