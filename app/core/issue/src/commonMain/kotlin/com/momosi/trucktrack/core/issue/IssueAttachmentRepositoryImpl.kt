package com.momosi.trucktrack.core.issue

import com.momosi.trucktrack.core.common.TruckTrackConfig
import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.core.common.model.Page
import com.momosi.trucktrack.core.issue.api.IssueAttachmentApi
import com.momosi.trucktrack.core.issue.dto.toIssueAttachment
import com.momosi.trucktrack.core.issue.model.IssueAttachment
import com.momosi.trucktrack.core.network.dto.toPage

class IssueAttachmentRepositoryImpl(private val issueAttachmentApi: IssueAttachmentApi) : IssueAttachmentRepository {

    override fun getPhotoUrl(issueId: Long, attachmentId: Long): String = "${TruckTrackConfig.API_BASE_URL}api/v1/issue/$issueId/photo/$attachmentId"

    override suspend fun getPhotos(
        issueId: Long,
        page: Int?,
        size: Int?,
        sort: String?,
    ): Result<Page<IssueAttachment>> = runCatching {
        issueAttachmentApi.getPhotoList(
            issueId = issueId,
            page = page,
            size = size,
            sort = sort,
        ).toPage { it.toIssueAttachment() }
    }.onFailure { Logger.e(TAG, it, "Failed to get photos for issue $issueId") }

    override suspend fun uploadPhoto(
        issueId: Long,
        fileName: String,
        fileBytes: ByteArray,
        contentType: String,
    ): Result<IssueAttachment> = runCatching {
        issueAttachmentApi.uploadPhoto(
            issueId = issueId,
            fileName = fileName,
            fileBytes = fileBytes,
            contentType = contentType,
        ).toIssueAttachment()
    }.onFailure { Logger.e(TAG, it, "Failed to upload photo for issue $issueId") }

    override suspend fun downloadPhoto(issueId: Long, attachmentId: Long): Result<ByteArray> = runCatching {
        issueAttachmentApi.downloadPhoto(issueId, attachmentId)
    }.onFailure { Logger.e(TAG, it, "Failed to download photo $attachmentId for issue $issueId") }
}
