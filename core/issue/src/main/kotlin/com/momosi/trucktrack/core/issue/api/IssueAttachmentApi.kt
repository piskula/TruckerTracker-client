package com.momosi.trucktrack.core.issue.api

import com.momosi.trucktrack.core.issue.dto.IssueAttachmentDto
import com.momosi.trucktrack.core.network.dto.PageDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface IssueAttachmentApi {

    @GET("api/v1/issue/{issueId}/photo")
    suspend fun getPhotoList(
        @Path("issueId") issueId: Long,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null,
    ): PageDto<IssueAttachmentDto>

    @Multipart
    @POST("api/v1/issue/{issueId}/photo")
    suspend fun uploadPhoto(@Path("issueId") issueId: Long, @Part file: MultipartBody.Part): IssueAttachmentDto

    @GET("api/v1/issue/{issueId}/photo/{attachmentId}")
    suspend fun downloadPhoto(@Path("issueId") issueId: Long, @Path("attachmentId") attachmentId: Long): ResponseBody
}
