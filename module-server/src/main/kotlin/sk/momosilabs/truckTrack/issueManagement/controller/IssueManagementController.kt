package sk.momosilabs.truckTrack.issueManagement.controller

import org.springframework.web.bind.annotation.RestController
import sk.momosilabs.truckTrack.api.common.PageDTO
import sk.momosilabs.truckTrack.api.common.PageableDTO
import sk.momosilabs.truckTrack.api.issue.IssueManagementApi
import sk.momosilabs.truckTrack.api.issue.dto.IssueCreateDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueFilterDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueHistoryDTO
import sk.momosilabs.truckTrack.issueManagement.service.addComment.AddCommentUseCase
import sk.momosilabs.truckTrack.issueManagement.service.assignToMe.AssignInProgressIssueToMeUseCase
import sk.momosilabs.truckTrack.issueManagement.service.createIssue.CreateIssueCommand
import sk.momosilabs.truckTrack.issueManagement.service.createIssue.CreateIssueUseCase
import sk.momosilabs.truckTrack.issueManagement.service.getIssue.GetIssueUseCase
import sk.momosilabs.truckTrack.issueManagement.service.getIssueHistory.GetIssueHistoryUseCase
import sk.momosilabs.truckTrack.issueManagement.service.getIssueList.GetIssueListUseCase
import sk.momosilabs.truckTrack.issueManagement.service.resolveIssue.ResolveIssueUseCase
import sk.momosilabs.truckTrack.issueManagement.service.startIssue.StartIssueUseCase
import sk.momosilabs.truckTrack.util.toDto
import sk.momosilabs.truckTrack.util.toModel

@RestController
class IssueManagementController(
    private val getIssueList: GetIssueListUseCase,
    private val getIssue: GetIssueUseCase,
    private val createIssue: CreateIssueUseCase,
    private val startIssue: StartIssueUseCase,
    private val assignInProgressIssueToMe: AssignInProgressIssueToMeUseCase,
    private val resolveIssue: ResolveIssueUseCase,
    private val getIssueHistory: GetIssueHistoryUseCase,
    private val addComment: AddCommentUseCase,
) : IssueManagementApi {

    override fun getIssueList(
        filter: IssueFilterDTO?,
        pageable: PageableDTO
    ): PageDTO<IssueDTO> =
        getIssueList.get(filter?.toModel(), pageable.toModel())
            .toDto { it.toDTO() }

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

    override fun assignToMe(id: Long): IssueDTO =
        assignInProgressIssueToMe.reassign(issueId = id).toDTO()

    override fun resolveIssue(id: Long): IssueDTO =
        resolveIssue.resolve(id).toDTO()

    override fun getIssueHistory(id: Long, pageable: PageableDTO): PageDTO<IssueHistoryDTO> =
        getIssueHistory.get(id, pageable.toModel()).toDto { it.toDTO() }

    override fun addComment(id: Long, text: String): IssueHistoryDTO =
        addComment.addComment(issueId = id, comment = text).toDTO()

}
