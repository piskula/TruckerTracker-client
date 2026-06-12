package sk.momosilabs.truckTrack.issue.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sk.momosilabs.truckTrack.issue.entity.IssueStatus
import sk.momosilabs.truckTrack.issue.model.IssueHistoryModel
import sk.momosilabs.truckTrack.issue.model.IssueModel
import java.time.OffsetDateTime

interface IssuePersistence {

    fun findPage(filter: IssueListFilter, pageable: Pageable): Page<IssueModel>

    fun findById(id: Long): IssueModel

    fun create(model: IssueModel): IssueModel

    fun updateStatus(id: Long, status: IssueStatus, updatedAt: OffsetDateTime): IssueModel

    fun saveHistory(model: IssueHistoryModel): IssueHistoryModel
}
