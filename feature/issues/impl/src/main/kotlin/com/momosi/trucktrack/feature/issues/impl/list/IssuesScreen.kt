package com.momosi.trucktrack.feature.issues.impl.list

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
import com.momosi.trucktrack.core.uilibrary.components.Text
import com.momosi.trucktrack.core.uilibrary.components.TopBarIconButton
import com.momosi.trucktrack.core.uilibrary.icons.TruckTrackIcons
import com.momosi.trucktrack.core.uilibrary.theme.AppTheme
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import com.momosi.trucktrack.core.vehicle.model.Vehicle
import com.momosi.trucktrack.core.vehicle.model.VehicleType
import com.momosi.trucktrack.feature.issues.impl.R
import com.momosi.trucktrack.user.model.User
import com.momosi.trucktrack.user.model.UserRole
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import java.time.Duration
import java.time.Instant

@Composable
internal fun IssuesScreen(
    viewModel: IssuesViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit,
    onNavigateToCreateIssue: () -> Unit,
    onNavigateToIssueDetail: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    IssuesScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToCreateIssue = onNavigateToCreateIssue,
        onNavigateToIssueDetail = onNavigateToIssueDetail,
    )
}

@Composable
private fun IssuesScreenContent(
    state: IssuesState,
    onAction: (IssuesAction) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCreateIssue: () -> Unit,
    onNavigateToIssueDetail: (Long) -> Unit,
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
//                    TopBarIconButton(icon = TruckTrackIcons.Search, onClick = {})
                    TopBarIconButton(icon = TruckTrackIcons.AccountCircle, onClick = onNavigateToProfile)
                },
            )
            FilterChipRow(
                items = if (isDriver) {
                    persistentListOf(
                        IssueFilter.Driver.MyOpen,
                        IssueFilter.Driver.MyClosed,
                        IssueFilter.Driver.All,
                    )
                } else {
                    persistentListOf(
                        IssueFilter.Mechanic.MyIssues,
                        IssueFilter.Mechanic.Open,
                        IssueFilter.Mechanic.All,
                    )
                },
                selectedItem = state.selectedFilter,
                labelSelector = { it.label() },
                onSelect = { onAction(IssuesAction.SelectFilter(it)) },
                modifier = Modifier.padding(bottom = 12.dp),
            )
            when (val content = state.content) {
                is IssuesContent.Loading -> LoadingContent()
                is IssuesContent.Error -> ErrorContent(onRetry = { onAction(IssuesAction.Retry) })
                is IssuesContent.Empty -> EmptyContent()
                is IssuesContent.Issues -> IssueList(
                    issues = content.issues,
                    onOpenIssue = onNavigateToIssueDetail,
                )
            }
        }
        if (isDriver) {
            FloatingActionButton(
                icon = TruckTrackIcons.Add,
                onClick = onNavigateToCreateIssue,
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
    onOpenIssue: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(
            items = issues,
            key = { it.id },
        ) { issue ->
            IssueCard(
                issue = issue,
                onClick = { onOpenIssue(issue.id) },
            )
        }
        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
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
private fun IssueFilter.label(): String = stringResource(
    when (this) {
        IssueFilter.Driver.MyOpen -> R.string.my_issues_filter_my_open
        IssueFilter.Driver.MyClosed -> R.string.my_issues_filter_my_closed
        IssueFilter.Driver.All -> R.string.my_issues_filter_all
        IssueFilter.Mechanic.MyIssues -> R.string.my_issues_filter_my_issues
        IssueFilter.Mechanic.Open -> R.string.my_issues_filter_open
        IssueFilter.Mechanic.All -> R.string.my_issues_filter_all
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
private fun IssuesDriverPreview() {
    TruckTrackTheme {
        IssuesScreenContent(
            state = IssuesState(
                user = User(id = "", name = "Michael Schumacher", email = "", role = UserRole.Driver),
                selectedFilter = IssueFilter.Driver.MyOpen,
                content = IssuesContent.Issues(previewIssues),
            ),
            onAction = {},
            onNavigateToProfile = {},
            onNavigateToCreateIssue = {},
            onNavigateToIssueDetail = {},
        )
    }
}

@Preview
@Composable
private fun IssuesMechanicPreview() {
    TruckTrackTheme {
        IssuesScreenContent(
            state = IssuesState(
                user = User(id = "", name = "Mattia Binotto", email = "", role = UserRole.Mechanic),
                selectedFilter = IssueFilter.Mechanic.MyIssues,
                content = IssuesContent.Issues(previewIssues),
            ),
            onAction = {},
            onNavigateToProfile = {},
            onNavigateToCreateIssue = {},
            onNavigateToIssueDetail = {},
        )
    }
}

@Preview
@Composable
private fun IssuesLoadingPreview() {
    TruckTrackTheme {
        IssuesScreenContent(
            state = IssuesState(content = IssuesContent.Loading),
            onAction = {},
            onNavigateToProfile = {},
            onNavigateToCreateIssue = {},
            onNavigateToIssueDetail = {},
        )
    }
}

@Preview
@Composable
private fun IssuesEmptyPreview() {
    TruckTrackTheme {
        IssuesScreenContent(
            state = IssuesState(
                user = User(id = "", name = "Test User", email = "", role = UserRole.Driver),
                content = IssuesContent.Empty,
            ),
            onAction = {},
            onNavigateToProfile = {},
            onNavigateToCreateIssue = {},
            onNavigateToIssueDetail = {},
        )
    }
}

// endregion
