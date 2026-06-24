package com.momosi.trucktrack.core.issue.api

import com.momosi.trucktrack.core.issue.dto.IssueAttachmentDto
import com.momosi.trucktrack.core.network.dto.PageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IssueAttachmentApi @Inject constructor(private val client: HttpClient) {

    suspend fun getPhotoList(
        issueId: Long,
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
    ): PageDto<IssueAttachmentDto> = client.get("api/v1/issue/$issueId/photo") {
        page?.let { parameter("page", it) }
        size?.let { parameter("size", it) }
        sort?.let { parameter("sort", it) }
    }.body()

    suspend fun uploadPhoto(
        issueId: Long,
        fileName: String,
        fileBytes: ByteArray,
        contentType: String,
    ): IssueAttachmentDto = client.submitFormWithBinaryData(
        url = "api/v1/issue/$issueId/photo",
        formData = formData {
            append(
                "file",
                fileBytes,
                Headers.build {
                    append(HttpHeaders.ContentType, contentType)
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                },
            )
        },
    ).body()

    suspend fun downloadPhoto(issueId: Long, attachmentId: Long): ByteArray =
        client.get("api/v1/issue/$issueId/photo/$attachmentId").bodyAsBytes()
}
