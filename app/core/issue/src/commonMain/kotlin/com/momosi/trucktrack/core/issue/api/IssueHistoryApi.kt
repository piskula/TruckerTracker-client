package com.momosi.trucktrack.core.issue.api

import com.momosi.trucktrack.shared.common.PageDto
import com.momosi.trucktrack.shared.issue.IssueHistoryDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface IssueHistoryApi {

    @GET("api/v1/issue/{id}/history")
    suspend fun getIssueHistory(
        @Path("id") id: Long,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null,
    ): PageDto<IssueHistoryDto>
}
