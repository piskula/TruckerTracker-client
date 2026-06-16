package com.momosi.trucktrack.feature.issues.impl.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.core.issue.IssueRepository
import com.momosi.trucktrack.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class IssuesViewModel @Inject constructor(
    userRepository: UserRepository,
    private val issueRepository: IssueRepository,
) : ViewModel() {

    private val selectedFilter = MutableStateFlow(StatusFilter.All)
    private val retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val content: Flow<IssuesContent> =
        combine(selectedFilter, retryTrigger.onStart { emit(Unit) }, userRepository.user) { filter, _, user -> Pair(filter, user) }
            .flatMapLatest { (filter, user) ->
                flow {
                    emit(IssuesContent.Loading)
                    issueRepository.getIssues(
                        statuses = listOfNotNull(filter.status),
                        accountIds = listOfNotNull(user?.id),
                    )
                        .onSuccess { page ->
                            val issues = page.content.toImmutableList()
                            emit(if (issues.isEmpty()) IssuesContent.Empty else IssuesContent.Issues(issues))
                        }
                        .onFailure {
                            emit(IssuesContent.Error)
                        }
                }
            }

    val state: StateFlow<IssuesState> = combine(
        userRepository.user,
        selectedFilter,
        content,
    ) { user, filter, content ->
        IssuesState(
            user = user,
            selectedFilter = filter,
            content = content,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = IssuesState(),
    )

    fun onAction(action: IssuesAction) {
        when (action) {
            is IssuesAction.SelectFilter -> selectedFilter.value = action.filter
            is IssuesAction.Retry -> retryTrigger.tryEmit(Unit)
            is IssuesAction.OpenIssue -> Unit
            is IssuesAction.CreateIssue -> Unit
        }
    }
}
