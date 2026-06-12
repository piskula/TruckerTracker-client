package sk.momosilabs.truckTrack.issue.persistence.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import sk.momosilabs.truckTrack.issue.entity.IssueAttachmentEntity

interface IssueAttachmentRepository : JpaRepository<IssueAttachmentEntity, Long> {
    fun findAllByIssueId(issueId: Long, pageable: Pageable): Page<IssueAttachmentEntity>
}
