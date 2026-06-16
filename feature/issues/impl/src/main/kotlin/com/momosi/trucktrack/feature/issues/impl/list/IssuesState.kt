package com.momosi.trucktrack.feature.issues.impl.list

import androidx.compose.runtime.Immutable
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.user.model.User
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class IssuesState(
    val user: User? = null,
    val selectedFilter: StatusFilter = StatusFilter.All,
    val content: IssuesContent = IssuesContent.Loading,
)

@Immutable
sealed interface IssuesContent {
    data object Loading : IssuesContent
    data object Error : IssuesContent
    data object Empty : IssuesContent

    @Immutable
    data class Issues(val issues: ImmutableList<Issue>) : IssuesContent
}
