package sk.momosilabs.truckTrack.issueManagement.service.getIssue

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.issueManagement.model.IssueModel
import sk.momosilabs.truckTrack.issueManagement.service.IssuePersistence
import sk.momosilabs.truckTrack.security.annotation.IsUser

@Service
class GetIssue(
    private val issuePersistence: IssuePersistence,
) : GetIssueUseCase {

    @IsUser
    @Transactional(readOnly = true)
    override fun get(issueId: Long): IssueModel =
        issuePersistence.findById(issueId)
}
