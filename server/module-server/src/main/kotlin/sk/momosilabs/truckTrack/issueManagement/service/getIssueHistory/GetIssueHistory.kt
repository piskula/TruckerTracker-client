package sk.momosilabs.truckTrack.issueManagement.service.getIssueHistory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.issueManagement.model.IssueHistoryModel
import sk.momosilabs.truckTrack.issueManagement.service.IssuePersistence
import sk.momosilabs.truckTrack.security.annotation.IsUser

@Service
class GetIssueHistory(
    private val issuePersistence: IssuePersistence,
) : GetIssueHistoryUseCase {

    @IsUser
    @Transactional(readOnly = true)
    override fun get(issueId: Long, pageable: Pageable): Page<IssueHistoryModel> =
        issuePersistence.findHistory(issueId, pageable)
}
