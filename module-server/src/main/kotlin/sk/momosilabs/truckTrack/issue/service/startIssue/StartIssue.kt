package sk.momosilabs.truckTrack.issue.service.startIssue

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
class StartIssue(
    private val issuePersistence: IssuePersistence,
    private val currentUserService: CurrentUserService,
) : StartIssueUseCase {

    @IsMechanic
    @Transactional
    override fun start(issueId: Long): IssueModel {
        val issue = issuePersistence.findById(issueId)
        check(issue.status == IssueStatus.OPEN) { "Issue must be OPEN to start, current status: ${issue.status}" }

        val mechanic: AccountModel = currentUserService.currentUser()

        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val saved = issuePersistence.updateStatus(issueId, IssueStatus.IN_PROGRESS, now)
        issuePersistence.saveHistory(
            IssueHistoryModel(
                issueId = saved.id,
                type = IssueHistoryEventType.STATUS_CHANGE,
                performedBy = mechanic,
                createdAt = now,
                statusFrom = IssueStatus.OPEN,
                statusTo = IssueStatus.IN_PROGRESS,
                commentText = null,
            )
        )
        return saved
    }
}
