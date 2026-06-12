package sk.momosilabs.truckTrack.issue.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.file.model.FileModel
import sk.momosilabs.truckTrack.file.persistence.repository.FileRepository
import sk.momosilabs.truckTrack.issue.entity.IssueAttachmentEntity
import sk.momosilabs.truckTrack.issue.model.IssueAttachmentModel
import sk.momosilabs.truckTrack.issue.persistence.mapper.toModel
import sk.momosilabs.truckTrack.issue.persistence.repository.IssueAttachmentRepository
import sk.momosilabs.truckTrack.issue.persistence.repository.IssueRepository
import sk.momosilabs.truckTrack.issue.service.IssueAttachmentPersistence
import sk.momosilabs.truckTrack.file.persistence.mapper.toModel

@Repository
class IssueAttachmentPersistenceProvider(
    private val issueAttachmentRepository: IssueAttachmentRepository,
    private val issueRepository: IssueRepository,
    private val fileRepository: FileRepository,
) : IssueAttachmentPersistence {

    @Transactional(readOnly = true)
    override fun findPage(issueId: Long, pageable: Pageable): Page<IssueAttachmentModel> =
        issueAttachmentRepository.findAllByIssueId(issueId, pageable).map { it.toModel() }

    @Transactional(readOnly = true)
    override fun findFileById(attachmentId: Long): FileModel {
        val entity = issueAttachmentRepository.findById(attachmentId)
            .orElseThrow { NoSuchElementException("attachment id=$attachmentId not found") }
        return entity.file.toModel()
    }

    @Transactional
    override fun create(issueId: Long, file: FileModel): IssueAttachmentModel {
        val entityToSave = IssueAttachmentEntity(
            issue = issueRepository.getReferenceById(issueId),
            file = fileRepository.getReferenceById(file.id),
        )
        return issueAttachmentRepository.save(entityToSave).toModel()
    }
}
