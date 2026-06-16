package com.momosi.trucktrack.user.internal.networking

import okhttp3.Request

private const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER = "Bearer"

internal fun Request.withAuthHeader(token: String): Request = newBuilder()
    .removeHeader(name = AUTHORIZATION_HEADER)
    .addHeader(name = AUTHORIZATION_HEADER, value = "$BEARER $token")
    .build()
