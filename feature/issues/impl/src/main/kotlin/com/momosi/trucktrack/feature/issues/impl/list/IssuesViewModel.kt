package com.momosi.trucktrack.feature.issues.impl.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.core.issue.IssueRepository
import com.momosi.trucktrack.core.issue.model.IssueStatus
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
    private val userRepository: UserRepository,
    private val issueRepository: IssueRepository,
) : ViewModel() {

    private val initialFilter: IssueFilter = when {
        userRepository.user.value?.isDualRole == true -> IssueFilter.DualRole.All
        userRepository.user.value?.isMechanic == true -> IssueFilter.Mechanic.MyIssues
        else -> IssueFilter.Driver.MyOpen
    }
    private val selectedFilter = MutableStateFlow<IssueFilter>(initialFilter)
    private val retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val _isRefreshing = MutableStateFlow(false)

    private val content: Flow<IssuesContent> =
        combine(selectedFilter, retryTrigger.onStart { emit(Unit) }, userRepository.user) { filter, _, user ->
            Triple(filter, user?.id, user?.roles)
        }.flatMapLatest { (filter, userId, _) ->
            flow {
                if (!_isRefreshing.value) emit(IssuesContent.Loading)
                val statuses = filter.statuses()
                val accountIds = filter.accountIds(userId)
                issueRepository.getIssues(
                    statuses = statuses,
                    accountIds = accountIds,
                )
                    .onSuccess { page ->
                        val issues = page.content.toImmutableList()
                        emit(if (issues.isEmpty()) IssuesContent.Empty else IssuesContent.Issues(issues))
                    }
                    .onFailure {
                        emit(IssuesContent.Error)
                    }
                _isRefreshing.value = false
            }
        }

    val state: StateFlow<IssuesState> = combine(
        userRepository.user,
        selectedFilter,
        content,
        _isRefreshing,
    ) { user, filter, content, isRefreshing ->
        IssuesState(
            userInfo = user?.let { IssuesUserInfo(name = it.name, roles = it.roles.toImmutableList()) },
            selectedFilter = filter,
            content = if (isRefreshing && content is IssuesContent.Issues) content.copy(isRefreshing = true) else content,
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
            is IssuesAction.Refresh -> {
                _isRefreshing.value = true
                retryTrigger.tryEmit(Unit)
            }
            is IssuesAction.OpenIssue -> Unit
            is IssuesAction.CreateIssue -> Unit
        }
    }
}

private fun IssueFilter.statuses(): List<IssueStatus> = when (this) {
    IssueFilter.Driver.MyOpen -> listOf(IssueStatus.Open, IssueStatus.InProgress)
    IssueFilter.Driver.MyClosed -> listOf(IssueStatus.Done)
    IssueFilter.Driver.All -> emptyList()
    IssueFilter.Mechanic.MyIssues -> listOf(IssueStatus.InProgress, IssueStatus.Done)
    IssueFilter.Mechanic.Open -> listOf(IssueStatus.Open)
    IssueFilter.Mechanic.All -> emptyList()
    IssueFilter.DualRole.Open -> listOf(IssueStatus.Open)
    IssueFilter.DualRole.InProgress -> listOf(IssueStatus.InProgress)
    IssueFilter.DualRole.All -> emptyList()
}

private fun IssueFilter.accountIds(userId: String?): List<String> = when (this) {
    IssueFilter.Driver.MyOpen -> listOfNotNull(userId)
    IssueFilter.Driver.MyClosed -> listOfNotNull(userId)
    IssueFilter.Driver.All -> emptyList()
    IssueFilter.Mechanic.MyIssues -> listOfNotNull(userId)
    IssueFilter.Mechanic.Open -> emptyList()
    IssueFilter.Mechanic.All -> emptyList()
    IssueFilter.DualRole.Open -> emptyList()
    IssueFilter.DualRole.InProgress -> emptyList()
    IssueFilter.DualRole.All -> emptyList()
}
