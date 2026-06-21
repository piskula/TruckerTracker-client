package com.momosi.trucktrack.feature.issues.impl.list

import androidx.compose.runtime.Immutable
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.user.model.UserRole
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class IssuesUserInfo(val name: String, val roles: ImmutableList<UserRole>) {
    val isDualRole: Boolean = UserRole.Driver in roles && UserRole.Mechanic in roles
    val isMechanic: Boolean = !isDualRole && UserRole.Mechanic in roles
}

@Immutable
data class IssuesState(val userInfo: IssuesUserInfo? = null, val selectedFilter: IssueFilter = IssueFilter.Driver.MyOpen, val content: IssuesContent = IssuesContent.Loading)

@Immutable
sealed interface IssuesContent {
    data object Loading : IssuesContent
    data object Error : IssuesContent
    data object Empty : IssuesContent

    @Immutable
    data class Issues(val issues: ImmutableList<Issue>, val isRefreshing: Boolean = false) : IssuesContent
}
