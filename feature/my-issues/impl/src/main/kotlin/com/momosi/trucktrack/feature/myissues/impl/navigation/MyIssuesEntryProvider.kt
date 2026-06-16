package com.momosi.trucktrack.feature.myissues.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.momosi.trucktrack.core.navigation.Navigator
import com.momosi.trucktrack.feature.myissues.api.MyIssuesNavKey
import com.momosi.trucktrack.feature.myissues.impl.MyIssuesScreen
import com.momosi.trucktrack.feature.profile.api.ProfileNavKey

fun EntryProviderScope<NavKey>.myIssuesEntries(navigator: Navigator) {
    entry<MyIssuesNavKey> {
        MyIssuesScreen(
            onNavigateToProfile = { navigator.navigate(ProfileNavKey) },
        )
    }
}
