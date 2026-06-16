package com.momosi.trucktrack.core.issue

import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.core.issue.model.IssueCreate
import com.momosi.trucktrack.core.issue.model.IssueHistory
import com.momosi.trucktrack.core.issue.model.IssuePriority
import com.momosi.trucktrack.core.issue.model.IssueStatus
import com.momosi.trucktrack.core.network.model.Page

interface IssueRepository {

    suspend fun getIssues(
        status: IssueStatus? = null,
        priority: IssuePriority? = null,
        vehicleId: Long? = null,
        search: String? = null,
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
    ): Result<Page<Issue>>

    suspend fun getIssue(id: Long): Result<Issue>

    suspend fun createIssue(issueCreate: IssueCreate): Result<Issue>

    suspend fun startIssue(id: Long): Result<Issue>

    suspend fun resolveIssue(id: Long): Result<Issue>

    suspend fun addComment(issueId: Long, comment: String): Result<IssueHistory>

    suspend fun getIssueHistory(
        issueId: Long,
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
    ): Result<Page<IssueHistory>>
}

