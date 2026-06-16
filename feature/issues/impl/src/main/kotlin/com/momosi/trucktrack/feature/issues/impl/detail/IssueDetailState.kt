package com.momosi.trucktrack.feature.issues.impl.detail

import androidx.compose.runtime.Immutable
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.core.issue.model.IssueAttachment
import com.momosi.trucktrack.core.issue.model.IssueHistory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class IssueDetailState(
    val content: IssueDetailContent = IssueDetailContent.Loading,
    val photos: ImmutableList<IssueAttachment> = persistentListOf(),
    val commentText: String = "",
    val isSendingComment: Boolean = false,
)

@Immutable
sealed interface IssueDetailContent {
    data object Loading : IssueDetailContent
    data object Error : IssueDetailContent

    @Immutable
    data class Loaded(
        val issue: Issue,
        val history: ImmutableList<IssueHistory> = persistentListOf(),
    ) : IssueDetailContent
}

