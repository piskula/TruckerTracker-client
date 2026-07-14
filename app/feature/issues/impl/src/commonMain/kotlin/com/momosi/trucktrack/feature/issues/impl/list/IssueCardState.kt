package com.momosi.trucktrack.feature.issues.impl.list

import androidx.compose.runtime.Immutable
import com.momosi.trucktrack.core.issue.model.Issue

@Immutable
internal data class IssueCardState(val issue: Issue, val role: IssueCardRole)

internal sealed interface IssueCardRole {
    data object Driver : IssueCardRole
    data object Mechanic : IssueCardRole
}
