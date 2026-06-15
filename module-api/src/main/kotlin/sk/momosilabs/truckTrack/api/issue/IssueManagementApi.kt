package sk.momosilabs.truckTrack.api.issue

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import sk.momosilabs.truckTrack.api.common.PageDTO
import sk.momosilabs.truckTrack.api.common.PageableDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueCreateDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueHistoryDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssuePriorityDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueStatusDTO

@Tag(name = "Issues")
interface IssueManagementApi {

    companion object {
        private const val ENDPOINT = "/api/v1/issue"
    }

    @Operation(summary = "Get issue list")
    @GetMapping(ENDPOINT)
    fun getIssueList(
        @RequestParam(required = false) status: IssueStatusDTO?,
        @RequestParam(required = false) priority: IssuePriorityDTO?,
        @RequestParam(required = false) vehicleId: Long?,
        @RequestParam(required = false) search: String?,
        @ParameterObject pageable: PageableDTO,
    ): PageDTO<IssueDTO>

    @Operation(summary = "Get issue by ID")
    @GetMapping("$ENDPOINT/{id}")
    fun getIssue(@PathVariable id: Long): IssueDTO

    @Operation(summary = "Create issue")
    @PostMapping(ENDPOINT)
    fun createIssue(@RequestBody request: IssueCreateDTO): IssueDTO

    @Operation(summary = "Start issue")
    @PostMapping("$ENDPOINT/{id}/start")
    fun startIssue(@PathVariable id: Long): IssueDTO

    @Operation(summary = "Resolve issue")
    @PostMapping("$ENDPOINT/{id}/resolve")
    fun resolveIssue(@PathVariable id: Long): IssueDTO

    @Operation(summary = "Get issue history")
    @GetMapping("$ENDPOINT/{id}/history")
    fun getIssueHistory(
        @PathVariable id: Long,
        @ParameterObject pageable: PageableDTO,
    ): PageDTO<IssueHistoryDTO>

    @Operation(summary = "Add comment")
    @PostMapping("$ENDPOINT/{id}/comment")
    fun addComment(@PathVariable id: Long, @RequestBody text: String): IssueHistoryDTO
}
