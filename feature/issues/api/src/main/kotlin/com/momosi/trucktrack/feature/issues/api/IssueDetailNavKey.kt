package com.momosi.trucktrack.feature.issues.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class IssueDetailNavKey(val issueId: Long) : NavKey

