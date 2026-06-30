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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momosi.trucktrack.core.common.formatter.DateFormatter
import com.momosi.trucktrack.core.issue.model.Account
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.core.issue.model.IssuePriority
import com.momosi.trucktrack.core.issue.model.IssueStatus
import com.momosi.trucktrack.core.uilibrary.components.Button
import com.momosi.trucktrack.core.uilibrary.components.DashboardTopBar
import com.momosi.trucktrack.core.uilibrary.components.FilterChipRow
import com.momosi.trucktrack.core.uilibrary.components.FloatingActionButton
import com.momosi.trucktrack.core.uilibrary.components.LoadingSpinner
import com.momosi.trucktrack.core.uilibrary.components.PullToRefresh
import com.momosi.trucktrack.core.uilibrary.components.Text
import com.momosi.trucktrack.core.uilibrary.components.TopBarIconButton
import com.momosi.trucktrack.core.uilibrary.icons.TruckTrackIcons
import com.momosi.trucktrack.core.uilibrary.theme.AppTheme
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import com.momosi.trucktrack.core.vehicle.model.Vehicle
import com.momosi.trucktrack.core.vehicle.model.VehicleType
import com.momosi.trucktrack.feature.issues.impl.resources.Res
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_empty
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_error_message
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_filter_all
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_filter_in_progress
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_filter_my_closed
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_filter_my_issues
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_filter_my_open
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_filter_open
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_retry
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_subtitle_driver
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_subtitle_mechanic
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_title_driver
import com.momosi.trucktrack.feature.issues.impl.resources.my_issues_title_mechanic
import com.momosi.trucktrack.user.model.UserRole
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.time.Clock
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun IssuesScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToCreateIssue: () -> Unit,
    onNavigateToIssueDetail: (Long) -> Unit,
    issueStatusChange: Boolean = false,
    viewModel: IssuesViewModel = koinViewModel(),
    dateFormatter: DateFormatter = koinInject(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(issueStatusChange) {
        if (issueStatusChange) {
            viewModel.onAction(IssuesAction.Refresh)
        }
    }

    IssuesScreenContent(
        state = state,
        dateFormatter = dateFormatter,
        onSelectFilter = { viewModel.onAction(IssuesAction.SelectFilter(it)) },
        onRetry = { viewModel.onAction(IssuesAction.Retry) },
        onRefresh = { viewModel.onAction(IssuesAction.Refresh) },
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToCreateIssue = onNavigateToCreateIssue,
        onNavigateToIssueDetail = onNavigateToIssueDetail,
    )
}

@Composable
private fun IssuesScreenContent(
    state: IssuesState,
    dateFormatter: DateFormatter,
    onSelectFilter: (IssueFilter) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCreateIssue: () -> Unit,
    onNavigateToIssueDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val userInfo = state.userInfo
    val isDualRole = userInfo?.isDualRole == true
    val isMechanic = !isDualRole && userInfo?.isMechanic == true
    val isDriver = !isDualRole && !isMechanic

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            DashboardTopBar(
                title = stringResource(
                    if (isDriver) Res.string.my_issues_title_driver else Res.string.my_issues_title_mechanic,
                ),
                subtitle = userInfo?.let { info ->
                    val roleLabel = info.roles.map { stringResource(it.labelRes()) }.joinToString(" · ")
                    "${info.name} · $roleLabel"
                }.orEmpty(),
                actions = {
                    TopBarIconButton(icon = TruckTrackIcons.AccountCircle, onClick = onNavigateToProfile)
                },
            )
            FilterChipRow(
                items = when {
                    isDualRole -> persistentListOf(
                        IssueFilter.DualRole.Open,
                        IssueFilter.DualRole.InProgress,
                        IssueFilter.DualRole.All,
                    )

                    isMechanic -> persistentListOf(
                        IssueFilter.Mechanic.MyIssues,
                        IssueFilter.Mechanic.Open,
                        IssueFilter.Mechanic.All,
                    )

                    else -> persistentListOf(
                        IssueFilter.Driver.MyOpen,
                        IssueFilter.Driver.MyClosed,
                        IssueFilter.Driver.All,
                    )
                },
                selectedItem = state.selectedFilter,
                labelSelector = { it.label() },
                onSelect = onSelectFilter,
            )
            when (val content = state.content) {
                is IssuesContent.Loading -> LoadingContent()

                is IssuesContent.Error -> ErrorContent(onRetry = onRetry)

                is IssuesContent.Empty -> EmptyContent()

                is IssuesContent.Issues -> IssueList(
                    content = content,
                    role = if (isMechanic) IssueCardRole.Mechanic else IssueCardRole.Driver,
                    dateFormatter = dateFormatter,
                    onOpenIssue = onNavigateToIssueDetail,
                    onRefresh = onRefresh,
                )
            }
        }
        if (isDriver || isDualRole) {
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
    content: IssuesContent.Issues,
    role: IssueCardRole,
    dateFormatter: DateFormatter,
    onOpenIssue: (Long) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefresh(
        isRefreshing = content.isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 4.dp, top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = content.issues,
                key = { it.id },
            ) { issue ->
                IssueCard(
                    state = IssueCardState(
                        issue = issue,
                        role = role,
                    ),
                    dateFormatter = dateFormatter,
                    onClick = { onOpenIssue(issue.id) },
                )
            }
            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
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
private fun ErrorContent(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(Res.string.my_issues_error_message),
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.onBackground,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(text = stringResource(Res.string.my_issues_retry), onClick = onRetry)
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
            text = stringResource(Res.string.my_issues_empty),
            style = AppTheme.typography.bodyLarge,
            color = AppTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun IssueFilter.label(): String = stringResource(
    when (this) {
        IssueFilter.Driver.MyOpen -> Res.string.my_issues_filter_my_open
        IssueFilter.Driver.MyClosed -> Res.string.my_issues_filter_my_closed
        IssueFilter.Driver.All -> Res.string.my_issues_filter_all
        IssueFilter.Mechanic.MyIssues -> Res.string.my_issues_filter_my_issues
        IssueFilter.Mechanic.Open -> Res.string.my_issues_filter_open
        IssueFilter.Mechanic.All -> Res.string.my_issues_filter_all
        IssueFilter.DualRole.Open -> Res.string.my_issues_filter_open
        IssueFilter.DualRole.InProgress -> Res.string.my_issues_filter_in_progress
        IssueFilter.DualRole.All -> Res.string.my_issues_filter_all
    },
)

private fun UserRole.labelRes(): StringResource = when (this) {
    UserRole.Driver -> Res.string.my_issues_subtitle_driver
    UserRole.Mechanic -> Res.string.my_issues_subtitle_mechanic
}

// region Previews

private val previewVehicle = Vehicle(
    id = 1,
    licensePlate = "MA-204-TT",
    make = "DAF",
    model = "XF",
    type = VehicleType.Truck,
)

private val previewReporter = Account(
    id = "1",
    username = "mschumacher",
    firstName = "Michael",
    lastName = "Schumacher",
)

private val previewNow = Clock.System.now()

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
        createdAt = previewNow.minus(kotlin.time.Duration.parse("2h")),
        updatedAt = previewNow,
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
        createdAt = previewNow.minus(kotlin.time.Duration.parse("5h")),
        updatedAt = previewNow,
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
        createdAt = previewNow.minus(kotlin.time.Duration.parse("24h")),
        updatedAt = previewNow,
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
        createdAt = previewNow.minus(kotlin.time.Duration.parse("600h")),
        updatedAt = previewNow,
    ),
).toImmutableList()

@Preview
@Composable
private fun IssuesDriverPreview() {
    TruckTrackTheme {
        IssuesScreenContent(
            state = IssuesState(
                userInfo = IssuesUserInfo(name = "Michael Schumacher", roles = persistentListOf(UserRole.Driver)),
                selectedFilter = IssueFilter.Driver.MyOpen,
                content = IssuesContent.Issues(previewIssues),
            ),
            dateFormatter = DateFormatter(),
            onSelectFilter = {},
            onRetry = {},
            onRefresh = {},
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
                userInfo = IssuesUserInfo(name = "Mattia Binotto", roles = persistentListOf(UserRole.Mechanic)),
                selectedFilter = IssueFilter.Mechanic.MyIssues,
                content = IssuesContent.Issues(previewIssues),
            ),
            dateFormatter = DateFormatter(),
            onSelectFilter = {},
            onRetry = {},
            onRefresh = {},
            onNavigateToProfile = {},
            onNavigateToCreateIssue = {},
            onNavigateToIssueDetail = {},
        )
    }
}

@Preview
@Composable
private fun IssuesDualRolePreview() {
    TruckTrackTheme {
        IssuesScreenContent(
            state = IssuesState(
                userInfo = IssuesUserInfo(name = "Lewis Hamilton", roles = persistentListOf(UserRole.Driver, UserRole.Mechanic)),
                selectedFilter = IssueFilter.DualRole.All,
                content = IssuesContent.Issues(previewIssues),
            ),
            dateFormatter = DateFormatter(),
            onSelectFilter = {},
            onRetry = {},
            onRefresh = {},
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
            dateFormatter = DateFormatter(),
            onSelectFilter = {},
            onRetry = {},
            onRefresh = {},
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
                userInfo = IssuesUserInfo(name = "Test User", roles = persistentListOf(UserRole.Driver)),
                content = IssuesContent.Empty,
            ),
            dateFormatter = DateFormatter(),
            onSelectFilter = {},
            onRetry = {},
            onRefresh = {},
            onNavigateToProfile = {},
            onNavigateToCreateIssue = {},
            onNavigateToIssueDetail = {},
        )
    }
}

// endregion
