package com.momosi.trucktrack.core.issue.dto

import kotlinx.serialization.Serializable

@Serializable
data class IssueHistoryDto(val id: String = "", val type: String = "", val performedBy: AccountDto? = null, val createdAt: String = "", val statusFrom: String? = null, val statusTo: String? = null, val commentText: String? = null)
