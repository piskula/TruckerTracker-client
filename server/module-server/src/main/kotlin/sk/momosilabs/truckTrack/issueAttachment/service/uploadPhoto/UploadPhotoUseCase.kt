package sk.momosilabs.truckTrack.issueAttachment.service.uploadPhoto

import sk.momosilabs.truckTrack.file.model.TruckTrackFile
import sk.momosilabs.truckTrack.issueAttachment.model.IssueAttachmentModel

interface UploadPhotoUseCase {
    fun upload(issueId: Long, file: TruckTrackFile): IssueAttachmentModel
}
