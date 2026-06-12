package sk.momosilabs.truckTrack.issue.service.resolveIssue

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.account.model.AccountModel
import sk.momosilabs.truckTrack.issue.entity.IssueHistoryEventType
import sk.momosilabs.truckTrack.issue.entity.IssueStatus
import sk.momosilabs.truckTrack.issue.model.IssueHistoryModel
import sk.momosilabs.truckTrack.issue.model.IssueModel
import sk.momosilabs.truckTrack.issue.service.IssuePersistence
import sk.momosilabs.truckTrack.security.CurrentUserService
import sk.momosilabs.truckTrack.security.annotation.IsMechanic
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class ResolveIssue(
    private val issuePersistence: IssuePersistence,
    private val currentUserService: CurrentUserService,
) : ResolveIssueUseCase {

    @IsMechanic
    @Transactional
    override fun resolve(issueId: Long): IssueModel {
        val issue = issuePersistence.findById(issueId)
        check(issue.status == IssueStatus.IN_PROGRESS) { "Issue must be IN_PROGRESS to resolve, current status: ${issue.status}" }

        val resolvedBy: AccountModel = currentUserService.currentUser()

        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val saved = issuePersistence.updateStatus(issueId, IssueStatus.DONE, now)
        issuePersistence.saveHistory(
            IssueHistoryModel(
                issueId = saved.id,
                type = IssueHistoryEventType.STATUS_CHANGE,
                performedBy = resolvedBy,
                createdAt = now,
                statusFrom = IssueStatus.IN_PROGRESS,
                statusTo = IssueStatus.DONE,
                commentText = null,
            )
        )
        return saved
    }
}
