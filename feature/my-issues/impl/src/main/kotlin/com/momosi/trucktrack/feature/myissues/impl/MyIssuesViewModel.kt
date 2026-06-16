package com.momosi.trucktrack.feature.myissues.impl

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
class MyIssuesViewModel @Inject constructor(
    userRepository: UserRepository,
    private val issueRepository: IssueRepository,
) : ViewModel() {

    private val selectedFilter = MutableStateFlow(StatusFilter.All)
    private val retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val content: Flow<MyIssuesContent> =
        combine(selectedFilter, retryTrigger.onStart { emit(Unit) }) { filter, _ -> filter }
            .flatMapLatest { filter ->
                flow {
                    emit(MyIssuesContent.Loading)
                    issueRepository.getIssues(status = filter.status)
                        .onSuccess { page ->
                            val issues = page.content.toImmutableList()
                            emit(if (issues.isEmpty()) MyIssuesContent.Empty else MyIssuesContent.Issues(issues))
                        }
                        .onFailure {
                            emit(MyIssuesContent.Error)
                        }
                }
            }

    val state: StateFlow<MyIssuesState> = combine(
        userRepository.user,
        selectedFilter,
        content,
    ) { user, filter, content ->
        MyIssuesState(
            user = user,
            selectedFilter = filter,
            content = content,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MyIssuesState(),
    )

    fun onAction(action: MyIssuesAction) {
        when (action) {
            is MyIssuesAction.SelectFilter -> selectedFilter.value = action.filter
            is MyIssuesAction.Retry -> retryTrigger.tryEmit(Unit)
            is MyIssuesAction.OpenIssue -> Unit
            is MyIssuesAction.CreateIssue -> Unit
        }
    }
}
