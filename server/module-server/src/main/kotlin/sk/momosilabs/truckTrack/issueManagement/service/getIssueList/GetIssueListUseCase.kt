package sk.momosilabs.truckTrack.issueManagement.service.getIssueList

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sk.momosilabs.truckTrack.issueManagement.model.IssueModel
import sk.momosilabs.truckTrack.issueManagement.service.IssueListFilter

interface GetIssueListUseCase {

    fun get(filter: IssueListFilter?, pageable: Pageable): Page<IssueModel>
}
