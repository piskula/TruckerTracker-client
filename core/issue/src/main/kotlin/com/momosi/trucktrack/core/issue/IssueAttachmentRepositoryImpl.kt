package com.momosi.trucktrack.core.issue

import com.momosi.trucktrack.core.issue.api.IssueAttachmentApi
import com.momosi.trucktrack.core.issue.dto.toIssueAttachment
import com.momosi.trucktrack.core.issue.model.IssueAttachment
import com.momosi.trucktrack.core.network.dto.toPage
import com.momosi.trucktrack.core.common.model.Page
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IssueAttachmentRepositoryImpl @Inject constructor(
    private val issueAttachmentApi: IssueAttachmentApi,
    private val retrofit: Retrofit,
) : IssueAttachmentRepository {

    override fun getPhotoUrl(issueId: Long, attachmentId: Long): String =
        "${retrofit.baseUrl()}api/v1/issue/$issueId/photo/$attachmentId"

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
        val requestBody = file.asRequestBody(contentType.toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        issueAttachmentApi.uploadPhoto(issueId, part).toIssueAttachment()
    }

    override suspend fun downloadPhoto(
        issueId: Long,
        attachmentId: Long,
    ): Result<ResponseBody> = runCatching {
        issueAttachmentApi.downloadPhoto(issueId, attachmentId)
    }
}

