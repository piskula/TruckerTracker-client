package com.momosi.trucktrack.core.issue

import com.momosi.trucktrack.core.common.TruckTrackConfig
import com.momosi.trucktrack.core.common.model.Page
import com.momosi.trucktrack.core.issue.api.IssueAttachmentApi
import com.momosi.trucktrack.core.issue.dto.toIssueAttachment
import com.momosi.trucktrack.core.issue.model.IssueAttachment
import com.momosi.trucktrack.core.network.dto.toPage
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class IssueAttachmentRepositoryImpl @Inject constructor(
    private val issueAttachmentApi: IssueAttachmentApi,
) : IssueAttachmentRepository {

    override fun getPhotoUrl(issueId: Long, attachmentId: Long): String =
        "${TruckTrackConfig.API_BASE_URL}api/v1/issue/$issueId/photo/$attachmentId"

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
    }

    override suspend fun uploadPhoto(
        issueId: Long,
        file: File,
        contentType: String,
    ): Result<IssueAttachment> = runCatching {
        issueAttachmentApi.uploadPhoto(
            issueId = issueId,
            fileName = file.name,
            fileBytes = file.readBytes(),
            contentType = contentType,
        ).toIssueAttachment()
    }

    override suspend fun downloadPhoto(issueId: Long, attachmentId: Long): Result<ByteArray> = runCatching {
        issueAttachmentApi.downloadPhoto(issueId, attachmentId)
    }
}
