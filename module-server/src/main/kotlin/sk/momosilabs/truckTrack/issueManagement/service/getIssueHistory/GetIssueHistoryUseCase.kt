package sk.momosilabs.truckTrack.issueManagement.service.getIssueHistory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sk.momosilabs.truckTrack.issueManagement.model.IssueHistoryModel

interface GetIssueHistoryUseCase {
    fun get(issueId: Long, pageable: Pageable): Page<IssueHistoryModel>
}
