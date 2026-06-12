package sk.momosilabs.truckTrack.issue.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import sk.momosilabs.truckTrack.issue.entity.IssueEntity
import sk.momosilabs.truckTrack.issue.entity.IssueHistoryEntity

interface IssueRepository : JpaRepository<IssueEntity, Long>, JpaSpecificationExecutor<IssueEntity>

interface IssueHistoryRepository : JpaRepository<IssueHistoryEntity, Long>
