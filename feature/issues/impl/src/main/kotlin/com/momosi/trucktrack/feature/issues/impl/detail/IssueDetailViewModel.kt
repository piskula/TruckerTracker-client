package com.momosi.trucktrack.feature.issues.impl.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.core.issue.IssueAttachmentRepository
import com.momosi.trucktrack.core.issue.IssueRepository
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.core.issue.model.IssueHistory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")

@HiltViewModel(assistedFactory = IssueDetailViewModel.Factory::class)
class IssueDetailViewModel @AssistedInject constructor(
    @Assisted private val issueId: Long,
    private val issueRepository: IssueRepository,
    private val issueAttachmentRepository: IssueAttachmentRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(issueId: Long): IssueDetailViewModel
    }

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
        when (action) {
            is IssueDetailAction.UpdateComment -> _state.update { it.copy(commentText = action.text) }
            is IssueDetailAction.SendComment -> sendComment()
            is IssueDetailAction.Retry -> loadIssueDetail()
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
            _state.update { it.copy(content = IssueDetailContent.Loaded(issue.toUi())) }
        }

        loadHistory()
        loadPhotos()
    }

    private fun loadHistory() {
        _state.update { it.copy(historyContent = IssueHistoryContent.Loading) }
        viewModelScope.launch {
            val items = issueRepository.getIssueHistory(issueId, sort = "createdAt,asc")
                .getOrNull()?.content
                ?.map { it.toUi() }
                ?.toImmutableList()
            _state.update {
                it.copy(
                    historyContent = if (items.isNullOrEmpty()) IssueHistoryContent.Empty
                                     else IssueHistoryContent.Loaded(items),
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

    private fun sendComment() {
        val text = _state.value.commentText.trim()
        if (text.isEmpty() || _state.value.isSendingComment) return

        _state.update { it.copy(isSendingComment = true) }

        viewModelScope.launch {
            issueRepository.addComment(issueId, text)
                .onSuccess {
                    _state.update { it.copy(commentText = "", isSendingComment = false) }
                    loadHistory()
                }
                .onFailure {
                    _state.update { it.copy(isSendingComment = false) }
                }
        }
    }
}

private fun Issue.toUi() = IssueUi(
    id = id,
    title = title,
    description = description,
    status = status,
    priority = priority,
    vehicleLabel = vehicle?.let { "${it.licensePlate} · ${it.make} ${it.model}" } ?: "",
    reportedByName = reportedBy?.fullName ?: "—",
    assignedToName = assignedTo?.fullName ?: "—",
    createdAtFormatted = createdAt.formatDate(),
)

private fun IssueHistory.toUi() = IssueHistoryUi(
    id = id,
    type = type,
    statusTo = statusTo,
    performedByName = performedBy?.fullName,
    createdAtFormatted = createdAt.formatDate(),
    commentText = commentText,
)

private fun Instant.formatDate(): String = dateFormatter.format(
    this.atZone(ZoneId.systemDefault()),
)
