package sk.momosilabs.truckTrack.issue.service.getIssue

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.issue.model.IssueModel
import sk.momosilabs.truckTrack.issue.service.IssuePersistence


@Service
class GetIssue(
    private val issuePersistence: IssuePersistence,
) : GetIssueUseCase {

    @Transactional(readOnly = true)
    override fun get(issueId: Long): IssueModel =
        issuePersistence.findById(issueId)
}
