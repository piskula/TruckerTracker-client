package sk.momosilabs.truckTrack.issue.service.getPhotoList

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.issue.model.IssueAttachmentModel
import sk.momosilabs.truckTrack.issue.service.IssueAttachmentPersistence

@Service
class GetPhotoList(
    private val issueAttachmentPersistence: IssueAttachmentPersistence,
) : GetPhotoListUseCase {

    @Transactional(readOnly = true)
    override fun list(issueId: Long, pageable: Pageable): Page<IssueAttachmentModel> =
        issueAttachmentPersistence.findPage(issueId, pageable)
}
