package sk.momosilabs.truckTrack.issueAttachment.service.getPhotoList

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.issueAttachment.model.IssueAttachmentModel
import sk.momosilabs.truckTrack.issueAttachment.service.IssueAttachmentPersistence
import sk.momosilabs.truckTrack.security.annotation.IsUser

@Service
class GetPhotoList(
    private val issueAttachmentPersistence: IssueAttachmentPersistence,
) : GetPhotoListUseCase {

    @IsUser
    @Transactional(readOnly = true)
    override fun list(issueId: Long, pageable: Pageable): Page<IssueAttachmentModel> =
        issueAttachmentPersistence.findPage(issueId, pageable)
}
