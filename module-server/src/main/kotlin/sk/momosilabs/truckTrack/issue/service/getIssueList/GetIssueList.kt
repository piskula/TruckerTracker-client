package sk.momosilabs.truckTrack.issue.service.getIssueList

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.issue.model.IssueModel
import sk.momosilabs.truckTrack.issue.service.IssuePersistence
import sk.momosilabs.truckTrack.issue.service.IssueListFilter


@Service
class GetIssueList(
    private val issuePersistence: IssuePersistence,
) : GetIssueListUseCase {

    @Transactional(readOnly = true)
    override fun get(filter: IssueListFilter, pageable: Pageable): Page<IssueModel> =
        issuePersistence.findPage(filter, pageable)
}
