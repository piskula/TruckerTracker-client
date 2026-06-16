package com.momosi.trucktrack.feature.issues.impl.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momosi.trucktrack.core.issue.model.Account
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.core.issue.model.IssueAttachment
import com.momosi.trucktrack.core.issue.model.IssueHistory
import com.momosi.trucktrack.core.issue.model.IssueHistoryType
import com.momosi.trucktrack.core.issue.model.IssuePriority
import com.momosi.trucktrack.core.issue.model.IssueStatus
import com.momosi.trucktrack.core.uilibrary.components.Button
import com.momosi.trucktrack.core.uilibrary.components.Icon
import com.momosi.trucktrack.core.uilibrary.components.LoadingSpinner
import com.momosi.trucktrack.core.uilibrary.components.Text
import com.momosi.trucktrack.core.uilibrary.components.Toolbar
import com.momosi.trucktrack.core.uilibrary.icons.TruckTrackIcons
import com.momosi.trucktrack.core.uilibrary.theme.AppTheme
import com.momosi.trucktrack.core.uilibrary.theme.Shapes
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import com.momosi.trucktrack.core.vehicle.model.Vehicle
import com.momosi.trucktrack.core.vehicle.model.VehicleType
import com.momosi.trucktrack.feature.issues.impl.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun IssueDetailScreen(
    issueId: Long,
    onBack: () -> Unit,
    viewModel: IssueDetailViewModel = hiltViewModel<IssueDetailViewModel, IssueDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(issueId) },
    ),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    IssueDetailContent(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}

@Composable
private fun IssueDetailContent(
    state: IssueDetailState,
    onAction: (IssueDetailAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(AppTheme.colors.background)) {
        when (val content = state.content) {
            is IssueDetailContent.Loading -> {
                Toolbar(title = "", onBack = onBack)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingSpinner()
                }
            }
            is IssueDetailContent.Error -> {
                Toolbar(title = "", onBack = onBack)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.issue_detail_error),
                            style = AppTheme.typography.bodyLarge,
                            color = AppTheme.colors.onBackground,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(text = stringResource(R.string.my_issues_retry), onClick = { onAction(IssueDetailAction.Retry) })
                    }
                }
            }
            is IssueDetailContent.Loaded -> {
                LoadedContent(
                    content = content,
                    photos = state.photos,
                    commentText = state.commentText,
                    isSendingComment = state.isSendingComment,
                    onAction = onAction,
                    onBack = onBack,
                )
            }
        }
    }
}

@Composable
private fun LoadedContent(
    content: IssueDetailContent.Loaded,
    photos: ImmutableList<IssueAttachment>,
    commentText: String,
    isSendingComment: Boolean,
    onAction: (IssueDetailAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val issue = content.issue
    val vehicleLabel = issue.vehicle?.let { "${it.licensePlate} · ${it.make} ${it.model}" } ?: ""

    Column(modifier = modifier.fillMaxSize()) {
        Toolbar(title = stringResource(R.string.issue_detail_title, issue.id), onBack = onBack)

        PeopleStrip(reportedBy = issue.reportedBy, assignedTo = issue.assignedTo)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HeaderCard(issue = issue, vehicleLabel = vehicleLabel)
            DescriptionCard(description = issue.description)
            HistoryCard(history = content.history)
            CommentCard(
                commentText = commentText,
                isSending = isSendingComment,
                onUpdateComment = { onAction(IssueDetailAction.UpdateComment(it)) },
                onSend = { onAction(IssueDetailAction.SendComment) },
            )
            if (photos.isNotEmpty()) {
                PhotosCard(photos = photos)
            }
        }
    }
}

@Composable
private fun PeopleStrip(
    reportedBy: Account?,
    assignedTo: Account?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.tertiary)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        PersonCell(
            role = stringResource(R.string.issue_detail_reporter),
            name = reportedBy?.fullName ?: "—",
            icon = TruckTrackIcons.Person,
            modifier = Modifier.weight(1f),
        )
        PersonCell(
            role = stringResource(R.string.issue_detail_assigned),
            name = assignedTo?.fullName ?: "—",
            icon = TruckTrackIcons.Build,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun PersonCell(
    role: String,
    name: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(AppTheme.colors.onPrimary.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = icon, tint = AppTheme.colors.onPrimary, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = role.uppercase(),
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colors.onPrimary.copy(alpha = 0.65f),
            )
            Text(
                text = name,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.onPrimary,
            )
        }
    }
}

@Composable
private fun HeaderCard(
    issue: Issue,
    vehicleLabel: String,
    modifier: Modifier = Modifier,
) {
    val borderColor = issue.priority.borderColor()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.CardShape)
            .background(AppTheme.colors.surface, Shapes.CardShape)
            .drawBehind {
                drawRect(
                    color = borderColor,
                    topLeft = Offset.Zero,
                    size = size.copy(width = 4.dp.toPx()),
                )
            }
            .padding(start = 12.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = issue.title,
                    style = AppTheme.typography.titleSmall,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier.weight(1f),
                )
                StatusChip(status = issue.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PriorityIndicator(priority = issue.priority)
                if (vehicleLabel.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                    ) {
                        Icon(imageVector = TruckTrackIcons.Truck, tint = AppTheme.colors.onSurfaceVariant, modifier = Modifier.size(15.dp))
                        Text(text = vehicleLabel, style = AppTheme.typography.bodySmall, color = AppTheme.colors.onSurfaceVariant)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = issue.createdAt.formatDate(),
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun StatusChip(status: IssueStatus, modifier: Modifier = Modifier) {
    val containerColor = status.containerColor()
    val contentColor = status.contentColor()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .background(containerColor, Shapes.CardShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Icon(imageVector = status.icon(), tint = contentColor, modifier = Modifier.size(13.dp))
        Text(text = status.displayName().uppercase(), style = AppTheme.typography.labelSmall, color = contentColor)
    }
}

@Composable
private fun PriorityIndicator(priority: IssuePriority, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier,
    ) {
        Icon(imageVector = priority.indicatorIcon(), tint = priority.borderColor(), modifier = Modifier.size(15.dp))
        Text(text = priority.displayName().uppercase(), style = AppTheme.typography.labelSmall, color = priority.borderColor())
    }
}

@Composable
private fun DescriptionCard(description: String, modifier: Modifier = Modifier) {
    CardContainer(title = stringResource(R.string.issue_detail_description), modifier = modifier) {
        Text(
            text = description,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.onSurface,
        )
    }
}

@Composable
private fun HistoryCard(history: ImmutableList<IssueHistory>, modifier: Modifier = Modifier) {
    CardContainer(title = stringResource(R.string.issue_detail_history), modifier = modifier) {
        Column {
            history.forEachIndexed { index, entry ->
                TimelineStep(entry = entry, isLast = index == history.lastIndex)
            }
        }
    }
}

@Composable
private fun TimelineStep(entry: IssueHistory, isLast: Boolean, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp),
        ) {
            when (entry.type) {
                IssueHistoryType.StatusChange -> {
                    val dotColor = entry.statusTo.dotColor()
                    Box(
                        modifier = Modifier.size(32.dp).background(dotColor, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = entry.statusTo.icon(),
                            tint = AppTheme.colors.onPrimary,
                            modifier = Modifier.size(17.dp),
                        )
                    }
                }
                IssueHistoryType.Comment -> {
                    Box(
                        modifier = Modifier.size(20.dp).background(AppTheme.colors.onSurfaceVariant, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = TruckTrackIcons.ChatBubbleOutline,
                            tint = AppTheme.colors.onPrimary,
                            modifier = Modifier.size(11.dp),
                        )
                    }
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(24.dp)
                        .background(AppTheme.colors.surfaceVariant),
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when (entry.type) {
                    IssueHistoryType.StatusChange -> {
                        Text(
                            text = entry.statusTo.displayName(),
                            style = AppTheme.typography.titleSmall,
                            color = AppTheme.colors.onSurface,
                        )
                    }
                    IssueHistoryType.Comment -> {
                        Text(
                            text = entry.commentText ?: "",
                            style = AppTheme.typography.bodySmall,
                            color = AppTheme.colors.onSurface,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                entry.performedBy?.let {
                    Text(
                        text = it.fullName,
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colors.onSurfaceVariant,
                    )
                }
            }
            Text(
                text = entry.createdAt.formatDate(),
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colors.surfaceVariant,
            )
        }
    }
}

@Composable
private fun CommentCard(
    commentText: String,
    isSending: Boolean,
    onUpdateComment: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CardContainer(title = stringResource(R.string.issue_detail_add_comment), modifier = modifier) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(AppTheme.colors.background, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
            ) {
                androidx.compose.foundation.text.BasicTextField(
                    value = commentText,
                    onValueChange = onUpdateComment,
                    textStyle = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.onSurface),
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { inner ->
                        if (commentText.isEmpty()) {
                            Text(
                                text = stringResource(R.string.issue_detail_comment_placeholder),
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colors.onSurfaceVariant,
                            )
                        }
                        inner()
                    },
                )
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(AppTheme.colors.primary, CircleShape)
                    .clip(CircleShape)
                    .clickable(enabled = !isSending && commentText.isNotBlank(), onClick = onSend),
                contentAlignment = Alignment.Center,
            ) {
                if (isSending) {
                    LoadingSpinner(size = 20.dp, strokeWidth = 2.dp)
                } else {
                    Icon(imageVector = TruckTrackIcons.Send, tint = AppTheme.colors.onPrimary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun PhotosCard(photos: ImmutableList<IssueAttachment>, modifier: Modifier = Modifier) {
    CardContainer(title = stringResource(R.string.issue_detail_photos, photos.size), modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            photos.forEach { photo ->
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(AppTheme.colors.surfaceVariant, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Text(
                        text = photo.filename,
                        style = AppTheme.typography.labelSmall,
                        color = AppTheme.colors.onPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                AppTheme.colors.onSurface.copy(alpha = 0.42f),
                                RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                            )
                            .padding(vertical = 2.dp, horizontal = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun CardContainer(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.surface, Shapes.CardShape)
            .padding(16.dp),
    ) {
        Text(
            text = title.uppercase(),
            style = AppTheme.typography.labelSmall,
            color = AppTheme.colors.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        content()
    }
}

// region Helpers

@Composable
private fun IssuePriority.borderColor(): Color = when (this) {
    IssuePriority.High -> AppTheme.colors.negative
    IssuePriority.Medium -> AppTheme.colors.warning
    IssuePriority.Low -> AppTheme.colors.primary
}

@Composable
private fun IssuePriority.displayName(): String = stringResource(
    when (this) {
        IssuePriority.High -> R.string.issue_priority_high
        IssuePriority.Medium -> R.string.issue_priority_medium
        IssuePriority.Low -> R.string.issue_priority_low
    },
)

private fun IssuePriority.indicatorIcon() = when (this) {
    IssuePriority.High -> TruckTrackIcons.ArrowUpward
    IssuePriority.Medium -> TruckTrackIcons.Remove
    IssuePriority.Low -> TruckTrackIcons.ArrowDownward
}

@Composable
private fun IssueStatus.containerColor(): Color = when (this) {
    IssueStatus.Open -> AppTheme.colors.primaryContainer
    IssueStatus.InProgress -> AppTheme.colors.warningContainer
    IssueStatus.Done -> AppTheme.colors.positiveContainer
}

@Composable
private fun IssueStatus.contentColor(): Color = when (this) {
    IssueStatus.Open -> AppTheme.colors.primary
    IssueStatus.InProgress -> AppTheme.colors.warning
    IssueStatus.Done -> AppTheme.colors.positive
}

private fun IssueStatus?.icon() = when (this) {
    IssueStatus.Open -> TruckTrackIcons.RadioButtonUnchecked
    IssueStatus.InProgress -> TruckTrackIcons.Build
    IssueStatus.Done -> TruckTrackIcons.CheckCircle
    null -> TruckTrackIcons.RadioButtonUnchecked
}

@Composable
private fun IssueStatus?.displayName(): String = when (this) {
    IssueStatus.Open -> stringResource(R.string.issue_status_open)
    IssueStatus.InProgress -> stringResource(R.string.issue_status_in_progress)
    IssueStatus.Done -> stringResource(R.string.issue_status_done)
    null -> ""
}

@Composable
private fun IssueStatus?.dotColor(): Color = when (this) {
    IssueStatus.Open -> AppTheme.colors.primary
    IssueStatus.InProgress -> AppTheme.colors.warning
    IssueStatus.Done -> AppTheme.colors.positive
    null -> AppTheme.colors.surfaceVariant
}

private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")

private fun Instant.formatDate(): String = dateFormatter.format(
    this.atZone(ZoneId.systemDefault()),
)

// endregion

// region Previews

private val previewVehicle = Vehicle(1, "MA-204-TT", "Volvo", "FH16", VehicleType.Truck)
private val previewReporter = Account("1", "mschumacher", "Michael", "Schumacher")
private val previewMechanic = Account("2", "mbinotto", "Mattia", "Binotto")

private val previewIssue = Issue(
    id = 1042,
    title = "Engine warning light — truck won't start",
    description = "Tried to start the engine this morning and the warning light came on. The truck won't start at all. Located at depot gate 3.",
    status = IssueStatus.InProgress,
    priority = IssuePriority.High,
    vehicle = previewVehicle,
    reportedBy = previewReporter,
    assignedTo = previewMechanic,
    createdAt = Instant.now().minus(Duration.ofHours(8)),
    updatedAt = Instant.now(),
)

private val previewHistory = listOf(
    IssueHistory("1", IssueHistoryType.StatusChange, previewReporter, Instant.now().minus(Duration.ofHours(8)), null, IssueStatus.Open, null),
    IssueHistory("2", IssueHistoryType.StatusChange, previewMechanic, Instant.now().minus(Duration.ofHours(7)), IssueStatus.Open, IssueStatus.InProgress, null),
    IssueHistory("3", IssueHistoryType.Comment, previewMechanic, Instant.now().minus(Duration.ofHours(6)), null, null, "Issue diagnosed, spare parts ordered"),
    IssueHistory("4", IssueHistoryType.Comment, previewMechanic, Instant.now().minus(Duration.ofHours(3)), null, null, "Parts order delayed, ETA tomorrow morning"),
).toImmutableList()

@Preview
@Composable
private fun IssueDetailLoadedPreview() {
    TruckTrackTheme {
        IssueDetailContent(
            state = IssueDetailState(
                content = IssueDetailContent.Loaded(
                    issue = previewIssue,
                    history = previewHistory,
                ),
            ),
            onAction = {},
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun IssueDetailLoadingPreview() {
    TruckTrackTheme {
        IssueDetailContent(
            state = IssueDetailState(),
            onAction = {},
            onBack = {},
        )
    }
}

// endregion

