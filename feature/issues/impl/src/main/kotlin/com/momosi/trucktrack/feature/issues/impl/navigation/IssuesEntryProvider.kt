package com.momosi.trucktrack.feature.issues.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.momosi.trucktrack.core.navigation.Navigator
import com.momosi.trucktrack.core.navigation.ResultStore
import com.momosi.trucktrack.feature.issues.api.IssuesNavKey
import com.momosi.trucktrack.feature.issues.impl.FullScreenPhotoScreen
import com.momosi.trucktrack.feature.issues.impl.list.IssuesScreen
import com.momosi.trucktrack.feature.issues.impl.create.CreateIssueScreen
import com.momosi.trucktrack.feature.issues.impl.detail.IssueDetailScreen
import com.momosi.trucktrack.feature.profile.api.ProfileNavKey

private object IssueStatusChangedKey

fun EntryProviderScope<NavKey>.issuesEntries(navigator: Navigator, resultStore: ResultStore) {
    entry<IssuesNavKey> {
        val statusChanged = resultStore[IssueStatusChangedKey] ?: false
        IssuesScreen(
            issueStatusChanged = statusChanged,
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
            onNavigateToFullScreenPhoto = { uri -> navigator.navigate(FullScreenPhotoNavKey(uri)) },
        )
    }
    entry<IssueDetailNavKey> { key ->
        IssueDetailScreen(
            issueId = key.issueId,
            onBack = { shouldReload ->
                resultStore[IssueStatusChangedKey] = shouldReload
                navigator.goBack()
            },
            onNavigateToFullScreenPhoto = { url -> navigator.navigate(FullScreenPhotoNavKey(url)) },
        )
    }
    entry<FullScreenPhotoNavKey> { key ->
        FullScreenPhotoScreen(
            photoUri = key.photoUri,
            onBack = navigator::goBack,
        )
    }
}
