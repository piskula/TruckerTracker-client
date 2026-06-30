package com.momosi.trucktrack.user.internal.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RealmDto(val realm: String, @SerialName("public_key") val publicKey: String)
