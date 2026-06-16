package com.momosi.trucktrack.user.internal.api

import retrofit2.http.GET

interface AuthApi {
    @GET("./")
    suspend fun getRealm(): RealmDto

}
