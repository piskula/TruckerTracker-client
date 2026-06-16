package com.momosi.trucktrack.core.issue.api

import com.momosi.trucktrack.core.issue.dto.IssueHistoryDto
import com.momosi.trucktrack.core.network.dto.PageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IssueHistoryApi {

    @GET("api/v1/issue/{id}/history")
    suspend fun getIssueHistory(
        @Path("id") id: Long,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null,
    ): PageDto<IssueHistoryDto>
}

