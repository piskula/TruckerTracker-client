package com.momosi.trucktrack.core.issue.api

import com.momosi.trucktrack.core.issue.dto.IssueCreateDto
import com.momosi.trucktrack.core.issue.dto.IssueDto
import com.momosi.trucktrack.core.issue.dto.IssueFilterDto
import com.momosi.trucktrack.core.issue.dto.IssueHistoryDto
import com.momosi.trucktrack.core.network.dto.PageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IssueApi {

    @POST("api/v1/issue")
    suspend fun getIssueList(
        @Body filter: IssueFilterDto,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null,
    ): PageDto<IssueDto>

    @POST("api/v1/issue/create")
    suspend fun createIssue(@Body body: IssueCreateDto): IssueDto

    @GET("api/v1/issue/byId/{id}")
    suspend fun getIssue(@Path("id") id: Long): IssueDto

    @POST("api/v1/issue/{id}/start")
    suspend fun startIssue(@Path("id") id: Long): IssueDto

    @POST("api/v1/issue/{id}/resolve")
    suspend fun resolveIssue(@Path("id") id: Long): IssueDto

    @POST("api/v1/issue/{id}/assignTome")
    suspend fun assignIssue(@Path("id") id: Long): IssueDto

    @POST("api/v1/issue/{id}/comment")
    suspend fun addComment(@Path("id") id: Long, @Body comment: String): IssueHistoryDto
}
