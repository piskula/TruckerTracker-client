package com.momosi.trucktrack.core.issue

import com.momosi.trucktrack.core.issue.api.IssueApi
import com.momosi.trucktrack.core.issue.api.IssueHistoryApi
import com.momosi.trucktrack.core.issue.dto.toDto
import com.momosi.trucktrack.core.issue.dto.toIssue
import com.momosi.trucktrack.core.issue.dto.toIssueHistory
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.core.issue.model.IssueCreate
import com.momosi.trucktrack.core.issue.model.IssueHistory
import com.momosi.trucktrack.core.issue.model.IssuePriority
import com.momosi.trucktrack.core.issue.model.IssueStatus
import com.momosi.trucktrack.core.network.dto.toPage
import com.momosi.trucktrack.core.network.model.Page
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IssueRepositoryImpl @Inject constructor(
    private val issueApi: IssueApi,
    private val issueHistoryApi: IssueHistoryApi,
) : IssueRepository {

    override suspend fun getIssues(
        status: IssueStatus?,
        priority: IssuePriority?,
        vehicleId: Long?,
        search: String?,
        page: Int?,
        size: Int?,
        sort: String?,
    ): Result<Page<Issue>> = runCatching {
        issueApi.getIssueList(
            status = status?.toApiValue(),
            priority = priority?.toApiValue(),
            vehicleId = vehicleId,
            search = search,
            page = page,
            size = size,
            sort = sort,
        ).toPage { it.toIssue() }
    }

    override suspend fun getIssue(id: Long): Result<Issue> = runCatching {
        issueApi.getIssue(id).toIssue()
    }

    override suspend fun createIssue(issueCreate: IssueCreate): Result<Issue> = runCatching {
        issueApi.createIssue(issueCreate.toDto()).toIssue()
    }

    override suspend fun startIssue(id: Long): Result<Issue> = runCatching {
        issueApi.startIssue(id).toIssue()
    }

    override suspend fun resolveIssue(id: Long): Result<Issue> = runCatching {
        issueApi.resolveIssue(id).toIssue()
    }

    override suspend fun addComment(issueId: Long, comment: String): Result<IssueHistory> = runCatching {
        issueApi.addComment(issueId, comment).toIssueHistory()
    }

    override suspend fun getIssueHistory(
        issueId: Long,
        page: Int?,
        size: Int?,
        sort: String?,
    ): Result<Page<IssueHistory>> = runCatching {
        issueHistoryApi.getIssueHistory(
            id = issueId,
            page = page,
            size = size,
            sort = sort,
        ).toPage { it.toIssueHistory() }
    }
}

