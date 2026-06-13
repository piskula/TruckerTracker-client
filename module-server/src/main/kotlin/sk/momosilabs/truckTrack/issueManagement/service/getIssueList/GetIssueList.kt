package sk.momosilabs.truckTrack.issueManagement.service.getIssueList

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.issueManagement.model.IssueModel
import sk.momosilabs.truckTrack.issueManagement.service.IssuePersistence
import sk.momosilabs.truckTrack.issueManagement.service.IssueListFilter
import sk.momosilabs.truckTrack.security.annotation.IsUser

@Service
class GetIssueList(
    private val issuePersistence: IssuePersistence,
) : GetIssueListUseCase {

    @IsUser
    @Transactional(readOnly = true)
    override fun get(filter: IssueListFilter, pageable: Pageable): Page<IssueModel> =
        issuePersistence.findPage(filter, pageable)
}
