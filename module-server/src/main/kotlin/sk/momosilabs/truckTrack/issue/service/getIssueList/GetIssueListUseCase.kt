package sk.momosilabs.truckTrack.issue.service.getIssueList

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sk.momosilabs.truckTrack.issue.model.IssueModel
import sk.momosilabs.truckTrack.issue.service.IssueListFilter

interface GetIssueListUseCase {

    fun get(filter: IssueListFilter, pageable: Pageable): Page<IssueModel>
}
