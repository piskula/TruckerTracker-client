package sk.momosilabs.truckTrack.issue.service.getPhotoList

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sk.momosilabs.truckTrack.issue.model.IssueAttachmentModel

interface GetPhotoListUseCase {
    fun list(issueId: Long, pageable: Pageable): Page<IssueAttachmentModel>
}
