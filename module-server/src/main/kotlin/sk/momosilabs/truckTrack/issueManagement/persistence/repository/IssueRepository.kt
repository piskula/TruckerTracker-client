package sk.momosilabs.truckTrack.issueManagement.persistence.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import sk.momosilabs.truckTrack.issueManagement.entity.IssueEntity
import sk.momosilabs.truckTrack.issueManagement.entity.IssueHistoryEntity

interface IssueRepository : JpaRepository<IssueEntity, Long>, JpaSpecificationExecutor<IssueEntity>

interface IssueHistoryRepository : JpaRepository<IssueHistoryEntity, Long> {
    fun findAllByIssueId(issueId: Long, pageable: Pageable): Page<IssueHistoryEntity>
}
