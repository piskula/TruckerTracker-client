package com.momosi.trucktrack.core.issue.api

import com.momosi.trucktrack.shared.common.PageDto
import com.momosi.trucktrack.shared.issue.IssueCreateDto
import com.momosi.trucktrack.shared.issue.IssueDto
import com.momosi.trucktrack.shared.issue.IssueFilterDto
import com.momosi.trucktrack.shared.issue.IssueHistoryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class IssueApi(private val client: HttpClient) {

    suspend fun getIssueList(
        filter: IssueFilterDto,
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
    ): PageDto<IssueDto> = client.post("api/v1/issue") {
        page?.let { parameter("page", it) }
        size?.let { parameter("size", it) }
        sort?.let { parameter("sort", it) }
        setBody(filter)
    }.body()

    suspend fun createIssue(body: IssueCreateDto): IssueDto = client.post("api/v1/issue/create") { setBody(body) }.body()

    suspend fun getIssue(id: Long): IssueDto = client.get("api/v1/issue/byId/$id").body()

    suspend fun startIssue(id: Long): IssueDto = client.post("api/v1/issue/$id/start").body()

    suspend fun resolveIssue(id: Long): IssueDto = client.post("api/v1/issue/$id/resolve").body()

    suspend fun assignIssue(id: Long): IssueDto = client.post("api/v1/issue/$id/assignTome").body()

    suspend fun addComment(id: Long, comment: String): IssueHistoryDto = client.post("api/v1/issue/$id/comment") { setBody(comment) }.body()
}
