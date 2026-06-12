package sk.momosilabs.truckTrack.issue.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sk.momosilabs.truckTrack.file.model.FileModel
import sk.momosilabs.truckTrack.issue.model.IssueAttachmentModel

interface IssueAttachmentPersistence {
    fun findPage(issueId: Long, pageable: Pageable): Page<IssueAttachmentModel>
    fun findFileById(attachmentId: Long): FileModel
    fun create(issueId: Long, file: FileModel): IssueAttachmentModel
}
