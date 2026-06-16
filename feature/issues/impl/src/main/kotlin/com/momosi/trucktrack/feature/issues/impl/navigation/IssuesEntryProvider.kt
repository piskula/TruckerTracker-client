package com.momosi.trucktrack.feature.issues.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.momosi.trucktrack.core.navigation.Navigator
import com.momosi.trucktrack.feature.issues.api.CreateIssueNavKey
import com.momosi.trucktrack.feature.issues.api.IssueDetailNavKey
import com.momosi.trucktrack.feature.issues.api.IssuesNavKey
import com.momosi.trucktrack.feature.issues.impl.list.IssuesScreen
import com.momosi.trucktrack.feature.issues.impl.create.CreateIssueScreen
import com.momosi.trucktrack.feature.issues.impl.detail.IssueDetailScreen
import com.momosi.trucktrack.feature.profile.api.ProfileNavKey

fun EntryProviderScope<NavKey>.issuesEntries(navigator: Navigator) {
    entry<IssuesNavKey> {
        IssuesScreen(
            onNavigateToProfile = { navigator.navigate(ProfileNavKey) },
            onNavigateToCreateIssue = { navigator.navigate(CreateIssueNavKey) },
            onNavigateToIssueDetail = { issueId -> navigator.navigate(IssueDetailNavKey(issueId)) },
        )
    }
    entry<CreateIssueNavKey> {
        CreateIssueScreen(
            onBack = navigator::goBack,
            onIssueCreated = { issueId ->
                navigator.goBack()
                navigator.navigate(IssueDetailNavKey(issueId))
            },
        )
    }
    entry<IssueDetailNavKey> { key ->
        IssueDetailScreen(
            issueId = key.issueId,
            onBack = navigator::goBack,
        )
    }
}
