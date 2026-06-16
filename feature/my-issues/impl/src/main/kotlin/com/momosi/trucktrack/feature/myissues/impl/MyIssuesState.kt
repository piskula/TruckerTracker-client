package com.momosi.trucktrack.feature.myissues.impl

import androidx.compose.runtime.Immutable
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.user.model.User
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class MyIssuesState(
    val user: User? = null,
    val selectedFilter: StatusFilter = StatusFilter.All,
    val content: MyIssuesContent = MyIssuesContent.Loading,
)

@Immutable
sealed interface MyIssuesContent {
    data object Loading : MyIssuesContent
    data object Error : MyIssuesContent
    data object Empty : MyIssuesContent

    @Immutable
    data class Issues(val issues: ImmutableList<Issue>) : MyIssuesContent
}
