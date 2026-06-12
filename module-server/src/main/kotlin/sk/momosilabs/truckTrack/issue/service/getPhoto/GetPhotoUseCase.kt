package sk.momosilabs.truckTrack.issue.service.getPhoto

import sk.momosilabs.truckTrack.file.model.TruckTrackFile

interface GetPhotoUseCase {
    fun get(attachmentId: Long): TruckTrackFile
}
