package sk.momosilabs.truckTrack.issueManagement.controller

import org.springframework.web.bind.annotation.RestController
import sk.momosilabs.truckTrack.account.model.AccountModel
import sk.momosilabs.truckTrack.api.common.PageDTO
import sk.momosilabs.truckTrack.api.common.PageableDTO
import sk.momosilabs.truckTrack.api.issue.IssueManagementApi
import sk.momosilabs.truckTrack.api.issue.dto.AccountDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueCreateDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueHistoryDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueHistoryEventTypeDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssuePriorityDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueStatusDTO
import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleDTO
import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleTypeDTO
import sk.momosilabs.truckTrack.issueManagement.entity.IssuePriority
import sk.momosilabs.truckTrack.issueManagement.entity.IssueStatus
import sk.momosilabs.truckTrack.issueManagement.model.IssueHistoryModel
import sk.momosilabs.truckTrack.issueManagement.model.IssueModel
import sk.momosilabs.truckTrack.issueManagement.service.IssueListFilter
import sk.momosilabs.truckTrack.issueManagement.service.addComment.AddCommentCommand
import sk.momosilabs.truckTrack.issueManagement.service.addComment.AddCommentUseCase
import sk.momosilabs.truckTrack.issueManagement.service.createIssue.CreateIssueCommand
import sk.momosilabs.truckTrack.issueManagement.service.createIssue.CreateIssueUseCase
import sk.momosilabs.truckTrack.issueManagement.service.getIssue.GetIssueUseCase
import sk.momosilabs.truckTrack.issueManagement.service.getIssueHistory.GetIssueHistoryUseCase
import sk.momosilabs.truckTrack.issueManagement.service.getIssueList.GetIssueListUseCase
import sk.momosilabs.truckTrack.issueManagement.service.resolveIssue.ResolveIssueUseCase
import sk.momosilabs.truckTrack.issueManagement.service.startIssue.StartIssueUseCase
import sk.momosilabs.truckTrack.util.toDto
import sk.momosilabs.truckTrack.util.toModel
import sk.momosilabs.truckTrack.vehicle.model.VehicleModel

@RestController
class IssueManagementController(
    private val getIssueList: GetIssueListUseCase,
    private val getIssue: GetIssueUseCase,
    private val createIssue: CreateIssueUseCase,
    private val startIssue: StartIssueUseCase,
    private val resolveIssue: ResolveIssueUseCase,
    private val getIssueHistory: GetIssueHistoryUseCase,
    private val addComment: AddCommentUseCase,
) : IssueManagementApi {

    override fun getIssueList(
        status: IssueStatusDTO?,
        priority: IssuePriorityDTO?,
        vehicleId: Long?,
        search: String?,
        pageable: PageableDTO,
    ): PageDTO<IssueDTO> =
        getIssueList.get(
            filter = IssueListFilter(
                status = status?.toModel(),
                priority = priority?.toModel(),
                vehicleId = vehicleId,
                search = search,
            ),
            pageable = pageable.toModel(),
        ).toDto { it.toDTO() }

    override fun getIssue(id: Long): IssueDTO =
        getIssue.get(id).toDTO()

    override fun createIssue(request: IssueCreateDTO): IssueDTO =
        createIssue.create(
            CreateIssueCommand(
                vehicleId = request.vehicleId,
                title = request.title,
                description = request.description,
                priority = request.priority.toModel(),
            )
        ).toDTO()

    override fun startIssue(id: Long): IssueDTO =
        startIssue.start(id).toDTO()

    override fun resolveIssue(id: Long): IssueDTO =
        resolveIssue.resolve(id).toDTO()

    override fun getIssueHistory(id: Long, pageable: PageableDTO): PageDTO<IssueHistoryDTO> =
        getIssueHistory.get(id, pageable.toModel()).toDto { it.toDTO() }

    override fun addComment(id: Long, text: String): IssueHistoryDTO =
        addComment.addComment(AddCommentCommand(issueId = id, text = text)).toDTO()
}

private fun IssueStatusDTO.toModel() = IssueStatus.valueOf(name)
private fun IssuePriorityDTO.toModel() = IssuePriority.valueOf(name)

private fun IssueModel.toDTO() = IssueDTO(
    id = id,
    title = title,
    description = description,
    status = IssueStatusDTO.valueOf(status.name),
    priority = IssuePriorityDTO.valueOf(priority.name),
    vehicle = vehicle.toDTO(),
    reportedBy = reportedBy.toDTO(),
    assignedTo = assignedTo?.toDTO(),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

private fun IssueHistoryModel.toDTO() = IssueHistoryDTO(
    id = id,
    type = IssueHistoryEventTypeDTO.valueOf(type.name),
    performedBy = performedBy.toDTO(),
    createdAt = createdAt,
    statusFrom = statusFrom?.let { IssueStatusDTO.valueOf(it.name) },
    statusTo = statusTo?.let { IssueStatusDTO.valueOf(it.name) },
    commentText = commentText,
)

private fun VehicleModel.toDTO() = VehicleDTO(
    id = id,
    licensePlate = licensePlate,
    make = make,
    model = model,
    type = VehicleTypeDTO.valueOf(type.name),
)

private fun AccountModel.toDTO() = AccountDTO(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
)
