package com.momosi.trucktrack.feature.issues.impl.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.core.common.formatter.DateFormatter
import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.core.issue.IssueAttachmentRepository
import com.momosi.trucktrack.core.issue.IssueRepository
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.core.issue.model.IssueHistory
import com.momosi.trucktrack.core.issue.model.IssueStatus
import com.momosi.trucktrack.user.UserRepository
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IssueDetailViewModel(
    private val issueId: Long,
    private val issueRepository: IssueRepository,
    private val issueAttachmentRepository: IssueAttachmentRepository,
    private val userRepository: UserRepository,
    private val dateFormatter: DateFormatter,
) : ViewModel() {

    private val _state = MutableStateFlow(IssueDetailState())
    val state: StateFlow<IssueDetailState> = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = IssueDetailState(),
    )

    init {
        loadIssueDetail()
    }

    fun onAction(action: IssueDetailAction) {
        Logger.i("Action:IssueDetail", action.toString())
        when (action) {
            is IssueDetailAction.UpdateComment -> _state.update { it.copy(commentText = action.text) }
            is IssueDetailAction.UploadPhoto -> uploadPhoto(action.file)
            is IssueDetailAction.SendComment -> sendComment()
            is IssueDetailAction.Retry -> loadIssueDetail()
            is IssueDetailAction.StartWorking -> startWorking()
            is IssueDetailAction.ResolveIssue -> resolveIssue()
            is IssueDetailAction.ReassignToMe -> reassignToMe()
        }
    }

    private fun loadIssueDetail() {
        _state.update {
            it.copy(
                content = IssueDetailContent.Loading,
                historyContent = IssueHistoryContent.Loading,
                photosContent = IssuePhotosContent.Loading,
            )
        }

        viewModelScope.launch {
            val issue = issueRepository.getIssue(issueId).getOrNull()
            if (issue == null) {
                _state.update { it.copy(content = IssueDetailContent.Error) }
                return@launch
            }
            _state.update {
                it.copy(
                    content = IssueDetailContent.Loaded(issue.toUi()),
                    mechanicAction = computeMechanicAction(issue),
                )
            }
        }

        loadHistory()
        loadPhotos()
    }

    private fun loadHistory(updateOnly: Boolean = false) {
        if (!updateOnly) {
            _state.update { it.copy(historyContent = IssueHistoryContent.Loading) }
        }
        viewModelScope.launch {
            val items = issueRepository.getIssueHistory(issueId)
                .getOrNull()?.content
                ?.map { it.toUi() }
                ?.toImmutableList()
            _state.update {
                it.copy(
                    historyContent = if (items.isNullOrEmpty()) {
                        IssueHistoryContent.Empty
                    } else {
                        IssueHistoryContent.Loaded(items)
                    },
                )
            }
        }
    }

    private fun loadPhotos() {
        _state.update { it.copy(photosContent = IssuePhotosContent.Loading) }
        viewModelScope.launch {
            val items = issueAttachmentRepository.getPhotos(issueId)
                .getOrNull()?.content
                ?.map { attachment ->
                    PhotoItem(
                        id = attachment.id,
                        filename = attachment.filename,
                        url = issueAttachmentRepository.getPhotoUrl(issueId, attachment.id),
                    )
                }
                ?.toImmutableList() ?: persistentListOf()
            _state.update { it.copy(photosContent = IssuePhotosContent.Loaded(items)) }
        }
    }

    private fun computeMechanicAction(issue: Issue): MechanicActionType? {
        val user = userRepository.user.value ?: return null
        if (!user.isMechanic) return null
        return when {
            issue.status == IssueStatus.Open && issue.assignedTo?.id != user.id -> MechanicActionType.StartWorking
            issue.status == IssueStatus.InProgress && issue.assignedTo?.id == user.id -> MechanicActionType.ResolveIssue
            issue.status == IssueStatus.InProgress && issue.assignedTo?.id != user.id -> MechanicActionType.Reassign
            else -> null
        }
    }

    private fun startWorking() {
        if (_state.value.isMechanicActionLoading) return
        _state.update { it.copy(isMechanicActionLoading = true) }
        viewModelScope.launch {
            issueRepository.startIssue(issueId)
                .onSuccess { issue ->
                    _state.update {
                        it.copy(
                            content = IssueDetailContent.Loaded(issue.toUi()),
                            mechanicAction = computeMechanicAction(issue),
                            isMechanicActionLoading = false,
                            statusChanged = true,
                        )
                    }
                    loadHistory(updateOnly = true)
                }
                .onFailure {
                    _state.update { it.copy(isMechanicActionLoading = false) }
                }
        }
    }

    private fun resolveIssue() {
        if (_state.value.isMechanicActionLoading) return
        _state.update { it.copy(isMechanicActionLoading = true) }
        viewModelScope.launch {
            issueRepository.resolveIssue(issueId)
                .onSuccess { issue ->
                    _state.update {
                        it.copy(
                            content = IssueDetailContent.Loaded(issue.toUi()),
                            mechanicAction = computeMechanicAction(issue),
                            isMechanicActionLoading = false,
                            statusChanged = true,
                        )
                    }
                    loadHistory(updateOnly = true)
                }
                .onFailure {
                    _state.update { it.copy(isMechanicActionLoading = false) }
                }
        }
    }

    private fun reassignToMe() {
        if (_state.value.isMechanicActionLoading) return
        _state.update { it.copy(isMechanicActionLoading = true) }
        viewModelScope.launch {
            issueRepository.assignIssue(issueId)
                .onSuccess { issue ->
                    _state.update {
                        it.copy(
                            content = IssueDetailContent.Loaded(issue.toUi()),
                            mechanicAction = computeMechanicAction(issue),
                            isMechanicActionLoading = false,
                        )
                    }
                    loadHistory(updateOnly = true)
                }
                .onFailure {
                    _state.update { it.copy(isMechanicActionLoading = false) }
                }
        }
    }

    private fun sendComment() {
        val text = _state.value.commentText.trim()
        if (text.isEmpty() || _state.value.isSendingComment) return

        _state.update { it.copy(isSendingComment = true) }

        viewModelScope.launch {
            issueRepository.addComment(issueId, text)
                .onSuccess {
                    _state.update { it.copy(commentText = "", isSendingComment = false) }
                    loadHistory(updateOnly = true)
                }
                .onFailure {
                    _state.update { it.copy(isSendingComment = false) }
                }
        }
    }

    private fun uploadPhoto(file: PlatformFile) {
        if (_state.value.isUploadingPhoto) return
        _state.update { it.copy(isUploadingPhoto = true) }
        viewModelScope.launch {
            issueAttachmentRepository.uploadPhoto(
                issueId = issueId,
                fileName = file.name,
                fileBytes = file.readBytes(),
                contentType = mimeTypeFromFileName(file.name),
            )
            _state.update { it.copy(isUploadingPhoto = false) }
            loadPhotos()
        }
    }

    private fun mimeTypeFromFileName(fileName: String): String = when (
        fileName.substringAfterLast('.', "").lowercase()
    ) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "heic", "heif" -> "image/heic"
        else -> "image/jpeg"
    }
    private fun Issue.toUi() = IssueUi(
        id = id,
        title = title,
        description = description,
        status = status,
        priority = priority,
        vehicleLabel = vehicle?.let { "${it.licensePlate} · ${it.make} ${it.model}" } ?: "",
        vehicleType = vehicle?.type,
        reportedByName = reportedBy?.fullName ?: "—",
        assignedToName = assignedTo?.fullName ?: "—",
        createdAtFormatted = dateFormatter.formatDateTime(createdAt),
    )

    private fun IssueHistory.toUi() = IssueHistoryUi(
        id = id,
        type = type,
        statusTo = statusTo,
        performedByName = performedBy?.fullName,
        createdAtFormatted = dateFormatter.formatDateTime(createdAt),
        commentText = commentText,
    )
}
