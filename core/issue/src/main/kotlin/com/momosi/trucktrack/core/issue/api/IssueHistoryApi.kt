package com.momosi.trucktrack.core.issue.api

import com.momosi.trucktrack.core.issue.dto.IssueHistoryDto
import com.momosi.trucktrack.core.network.dto.PageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class IssueHistoryApi(private val client: HttpClient) {

    suspend fun getIssueHistory(
        id: Long,
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
    ): PageDto<IssueHistoryDto> = client.get("api/v1/issue/$id/history") {
        page?.let { parameter("page", it) }
        size?.let { parameter("size", it) }
        sort?.let { parameter("sort", it) }
    }.body()
}
