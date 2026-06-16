package com.momosi.trucktrack.user.internal.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RealmDto(
    @SerialName("public_key") val publicKey: String,
)
