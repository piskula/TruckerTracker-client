package sk.momosilabs.truckTrack.issue.persistence

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.account.persistence.repository.AccountRepository
import sk.momosilabs.truckTrack.issue.entity.IssueEntity
import sk.momosilabs.truckTrack.issue.entity.IssueHistoryEntity
import sk.momosilabs.truckTrack.issue.entity.IssueStatus
import java.time.OffsetDateTime
import sk.momosilabs.truckTrack.issue.model.IssueHistoryModel
import sk.momosilabs.truckTrack.issue.model.IssueModel
import sk.momosilabs.truckTrack.issue.persistence.mapper.toModel
import sk.momosilabs.truckTrack.issue.persistence.repository.IssueHistoryRepository
import sk.momosilabs.truckTrack.issue.persistence.repository.IssueRepository
import sk.momosilabs.truckTrack.issue.service.IssuePersistence
import sk.momosilabs.truckTrack.issue.service.IssueListFilter
import sk.momosilabs.truckTrack.vehicle.persistence.repository.VehicleRepository

@Repository
class IssuePersistenceProvider(
    private val issueRepository: IssueRepository,
    private val issueHistoryRepository: IssueHistoryRepository,
    private val vehicleRepository: VehicleRepository,
    private val accountRepository: AccountRepository,
) : IssuePersistence {

    @Transactional(readOnly = true)
    override fun findPage(filter: IssueListFilter, pageable: Pageable): Page<IssueModel> {
        var spec = Specification<IssueEntity> { _, _, cb -> cb.conjunction() }
        if (filter.status != null)
            spec = spec.and(Specification { root, _, cb -> cb.equal(root.get<Any>("status"), filter.status) })
        if (filter.priority != null)
            spec = spec.and(Specification { root, _, cb -> cb.equal(root.get<Any>("priority"), filter.priority) })
        if (filter.vehicleId != null)
            spec = spec.and(Specification { root, _, cb -> cb.equal(root.get<Any>("vehicle").get<Long>("id"), filter.vehicleId) })
        if (filter.search != null) {
            val pattern = "%${filter.search.lowercase()}%"
            spec = spec.and(Specification { root, _, cb ->
                cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern),
                )
            })
        }
        return issueRepository.findAll(spec, pageable).map { it.toModel() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): IssueModel =
        issueRepository.getReferenceById(id).toModel()

    @Transactional
    override fun create(model: IssueModel): IssueModel {
        val entityToSave = IssueEntity(
            title = model.title,
            description = model.description,
            status = model.status,
            priority = model.priority,
            vehicle = vehicleRepository.getReferenceById(model.vehicle.id),
            reportedBy = accountRepository.getReferenceById(model.reportedBy.id),
            assignedTo = model.assignedTo?.let { accountRepository.getReferenceById(it.id) },
            createdAtUtc = model.createdAt.toLocalDateTime(),
            updatedAtUtc = model.updatedAt.toLocalDateTime(),
        )
        return issueRepository.save(entityToSave).toModel()
    }

    @Transactional
    override fun updateStatus(id: Long, status: IssueStatus, updatedAt: OffsetDateTime): IssueModel {
        val entity = issueRepository.findById(id)
            .orElseThrow { NoSuchElementException("issue id=$id not found") }
        entity.status = status
        entity.updatedAtUtc = updatedAt.toLocalDateTime()
        return entity.toModel()
    }

    @Transactional
    override fun saveHistory(model: IssueHistoryModel): IssueHistoryModel {
        val entityToSave = IssueHistoryEntity(
            issue = issueRepository.getReferenceById(model.issueId),
            type = model.type,
            performedBy = accountRepository.getReferenceById(model.performedBy.id),
            createdAtUtc = model.createdAt.toLocalDateTime(),
            statusFrom = model.statusFrom,
            statusTo = model.statusTo,
            commentText = model.commentText,
        )
        return issueHistoryRepository.save(entityToSave).toModel()
    }

}
