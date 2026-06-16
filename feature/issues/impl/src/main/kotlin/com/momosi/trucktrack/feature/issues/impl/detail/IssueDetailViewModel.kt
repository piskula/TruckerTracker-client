package com.momosi.trucktrack.feature.issues.impl.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.core.issue.IssueAttachmentRepository
import com.momosi.trucktrack.core.issue.IssueRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        _state.update { it.copy(content = IssueDetailContent.Loading, photos = kotlinx.collections.immutable.persistentListOf()) }

        viewModelScope.launch {
            val issueResult = issueRepository.getIssue(issueId)
            val historyResult = issueRepository.getIssueHistory(issueId, sort = "createdAt,asc")

            val issue = issueResult.getOrNull()
            if (issue == null) {
                _state.update { it.copy(content = IssueDetailContent.Error) }
                return@launch
            }

            _state.update {
                it.copy(
                    content = IssueDetailContent.Loaded(
                        issue = issue,
                        history = historyResult.getOrNull()?.content?.toImmutableList()
                            ?: kotlinx.collections.immutable.persistentListOf(),
                    ),
                )
            }
        }

        viewModelScope.launch {
            val photos = issueAttachmentRepository.getPhotos(issueId).getOrNull()?.content?.toImmutableList()
                ?: return@launch
            _state.update { it.copy(photos = photos) }
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
                    loadIssueDetail()
                }
                .onFailure {
                    _state.update { it.copy(isSendingComment = false) }
                }
        }
    }
}

