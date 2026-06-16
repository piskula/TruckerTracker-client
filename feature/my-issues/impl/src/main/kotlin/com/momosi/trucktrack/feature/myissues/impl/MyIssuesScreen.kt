package com.momosi.trucktrack.feature.myissues.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momosi.trucktrack.core.issue.model.Account
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.core.issue.model.IssuePriority
import com.momosi.trucktrack.core.issue.model.IssueStatus
import com.momosi.trucktrack.core.uilibrary.components.Button
import com.momosi.trucktrack.core.uilibrary.components.DashboardTopBar
import com.momosi.trucktrack.core.uilibrary.components.FilterChipRow
import com.momosi.trucktrack.core.uilibrary.components.FloatingActionButton
import com.momosi.trucktrack.core.uilibrary.components.LoadingSpinner
import com.momosi.trucktrack.core.uilibrary.components.SectionLabel
import com.momosi.trucktrack.core.uilibrary.components.Text
import com.momosi.trucktrack.core.uilibrary.components.TopBarIconButton
import com.momosi.trucktrack.core.uilibrary.icons.TruckTrackIcons
import com.momosi.trucktrack.core.uilibrary.theme.AppTheme
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import com.momosi.trucktrack.core.vehicle.model.Vehicle
import com.momosi.trucktrack.core.vehicle.model.VehicleType
import com.momosi.trucktrack.user.model.User
import com.momosi.trucktrack.user.model.UserRole
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import java.time.Duration
import java.time.Instant

@Composable
internal fun MyIssuesScreen(
    viewModel: MyIssuesViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MyIssuesContent(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToProfile = onNavigateToProfile,
    )
}

@Composable
private fun MyIssuesContent(
    state: MyIssuesState,
    onAction: (MyIssuesAction) -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val user = state.user
    val userRole = user?.role
    val isDriver = userRole == UserRole.Driver

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            DashboardTopBar(
                title = stringResource(
                    if (isDriver) R.string.my_issues_title_driver else R.string.my_issues_title_mechanic,
                ),
                subtitle = buildSubtitle(user),
                actions = {
                    TopBarIconButton(icon = TruckTrackIcons.Search, onClick = {})
                    TopBarIconButton(icon = TruckTrackIcons.AccountCircle, onClick = onNavigateToProfile)
                },
            )
            FilterChipRow(
                items = persistentListOf(
                    StatusFilter.All,
                    StatusFilter.Open,
                    StatusFilter.InProgress,
                    StatusFilter.Done,
                ),
                selectedItem = state.selectedFilter,
                labelSelector = { it.label() },
                onSelect = { onAction(MyIssuesAction.SelectFilter(it)) },
                modifier = Modifier.padding(bottom = 12.dp),
            )
            when (val content = state.content) {
                is MyIssuesContent.Loading -> LoadingContent()
                is MyIssuesContent.Error -> ErrorContent(onRetry = { onAction(MyIssuesAction.Retry) })
                is MyIssuesContent.Empty -> EmptyContent()
                is MyIssuesContent.Issues -> IssueList(
                    issues = content.issues,
                    userRole = userRole ?: UserRole.Driver,
                    selectedFilter = state.selectedFilter,
                    onOpenIssue = { onAction(MyIssuesAction.OpenIssue(it)) },
                )
            }
        }
        if (isDriver) {
            FloatingActionButton(
                icon = TruckTrackIcons.Add,
                onClick = { onAction(MyIssuesAction.CreateIssue) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 20.dp),
            )
        }
    }
}

@Composable
private fun IssueList(
    issues: ImmutableList<Issue>,
    userRole: UserRole,
    selectedFilter: StatusFilter,
    onOpenIssue: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val groupedIssues = groupIssues(issues, userRole, selectedFilter)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        groupedIssues.forEach { (label, sectionIssues) ->
            item(key = label) {
                SectionLabel(text = label)
            }
            items(
                items = sectionIssues,
                key = { it.id },
            ) { issue ->
                IssueCard(
                    issue = issue,
                    userRole = userRole,
                    onClick = { onOpenIssue(issue.id) },
                )
            }
        }
        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

@Composable
private fun groupIssues(
    issues: List<Issue>,
    userRole: UserRole,
    selectedFilter: StatusFilter,
): List<Pair<String, List<Issue>>> {
    if (selectedFilter != StatusFilter.All) {
        return listOf(selectedFilter.label() to issues)
    }
    val groups = mutableListOf<Pair<String, List<Issue>>>()
    if (userRole == UserRole.Mechanic) {
        val inProgress = issues.filter { it.status == IssueStatus.InProgress }
        val open = issues.filter { it.status == IssueStatus.Open }
        val done = issues.filter { it.status == IssueStatus.Done }
        if (inProgress.isNotEmpty()) groups.add(stringResource(R.string.my_issues_section_in_progress, inProgress.size) to inProgress)
        if (open.isNotEmpty()) groups.add(stringResource(R.string.my_issues_section_open_available, open.size) to open)
        if (done.isNotEmpty()) groups.add(stringResource(R.string.my_issues_section_completed, done.size) to done)
    } else {
        val active = issues.filter { it.status != IssueStatus.Done }
        val done = issues.filter { it.status == IssueStatus.Done }
        if (active.isNotEmpty()) groups.add(stringResource(R.string.my_issues_section_reported_by_me, active.size) to active)
        if (done.isNotEmpty()) groups.add(stringResource(R.string.my_issues_section_completed, done.size) to done)
    }
    return groups
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LoadingSpinner()
    }
}

@Composable
private fun ErrorContent(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.my_issues_error_message),
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.onBackground,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(text = stringResource(R.string.my_issues_retry), onClick = onRetry)
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.my_issues_empty),
            style = AppTheme.typography.bodyLarge,
            color = AppTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun buildSubtitle(user: User?): String {
    if (user == null) return ""
    val role = stringResource(
        when (user.role) {
            UserRole.Driver -> R.string.my_issues_subtitle_driver
            UserRole.Mechanic -> R.string.my_issues_subtitle_mechanic
        },
    )
    return "${user.name} · $role"
}

@Composable
private fun StatusFilter.label(): String = stringResource(
    when (this) {
        StatusFilter.All -> R.string.my_issues_filter_all
        StatusFilter.Open -> R.string.my_issues_filter_open
        StatusFilter.InProgress -> R.string.my_issues_filter_in_progress
        StatusFilter.Done -> R.string.my_issues_filter_done
    },
)

// region Previews

private val previewVehicle = Vehicle(
    id = 1, licensePlate = "MA-204-TT", make = "DAF", model = "XF", type = VehicleType.Truck,
)

private val previewReporter = Account(
    id = "1", username = "mschumacher", firstName = "Michael", lastName = "Schumacher",
)

private val previewIssues = listOf(
    Issue(
        id = 1,
        title = "Engine warning light — truck won't start",
        description = "",
        status = IssueStatus.InProgress,
        priority = IssuePriority.High,
        vehicle = previewVehicle,
        reportedBy = previewReporter,
        assignedTo = null,
        createdAt = Instant.now().minus(Duration.ofHours(2)),
        updatedAt = Instant.now(),
    ),
    Issue(
        id = 2,
        title = "Left rear tire pressure critically low",
        description = "",
        status = IssueStatus.Open,
        priority = IssuePriority.High,
        vehicle = previewVehicle.copy(id = 2, licensePlate = "MA-118-AB"),
        reportedBy = previewReporter,
        assignedTo = null,
        createdAt = Instant.now().minus(Duration.ofHours(5)),
        updatedAt = Instant.now(),
    ),
    Issue(
        id = 3,
        title = "Windshield wiper blade broken",
        description = "",
        status = IssueStatus.Open,
        priority = IssuePriority.Medium,
        vehicle = previewVehicle.copy(id = 3, licensePlate = "MA-337-PK"),
        reportedBy = previewReporter,
        assignedTo = null,
        createdAt = Instant.now().minus(Duration.ofDays(1)),
        updatedAt = Instant.now(),
    ),
    Issue(
        id = 4,
        title = "Routine oil change service",
        description = "",
        status = IssueStatus.Done,
        priority = IssuePriority.Low,
        vehicle = previewVehicle.copy(id = 2, licensePlate = "MA-118-AB"),
        reportedBy = previewReporter,
        assignedTo = null,
        createdAt = Instant.now().minus(Duration.ofDays(25)),
        updatedAt = Instant.now(),
    ),
).toImmutableList()

@Preview
@Composable
private fun MyIssuesDriverPreview() {
    TruckTrackTheme {
        MyIssuesContent(
            state = MyIssuesState(
                user = User(name = "Michael Schumacher", email = "", role = UserRole.Driver),
                content = MyIssuesContent.Issues(previewIssues),
            ),
            onAction = {},
            onNavigateToProfile = {},
        )
    }
}

@Preview
@Composable
private fun MyIssuesMechanicPreview() {
    TruckTrackTheme {
        MyIssuesContent(
            state = MyIssuesState(
                user = User(name = "Mattia Binotto", email = "", role = UserRole.Mechanic),
                content = MyIssuesContent.Issues(previewIssues),
            ),
            onAction = {},
            onNavigateToProfile = {},
        )
    }
}

@Preview
@Composable
private fun MyIssuesLoadingPreview() {
    TruckTrackTheme {
        MyIssuesContent(
            state = MyIssuesState(content = MyIssuesContent.Loading),
            onAction = {},
            onNavigateToProfile = {},
        )
    }
}

@Preview
@Composable
private fun MyIssuesErrorPreview() {
    TruckTrackTheme {
        MyIssuesContent(
            state = MyIssuesState(content = MyIssuesContent.Error),
            onAction = {},
            onNavigateToProfile = {},
        )
    }
}

@Preview
@Composable
private fun MyIssuesEmptyPreview() {
    TruckTrackTheme {
        MyIssuesContent(
            state = MyIssuesState(
                user = User(name = "Test User", email = "", role = UserRole.Driver),
                content = MyIssuesContent.Empty,
            ),
            onAction = {},
            onNavigateToProfile = {},
        )
    }
}

// endregion
