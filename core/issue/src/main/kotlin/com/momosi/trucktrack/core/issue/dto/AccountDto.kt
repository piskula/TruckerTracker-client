package com.momosi.trucktrack.core.issue.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(val id: String = "", val username: String = "", val firstName: String = "", val lastName: String = "")
