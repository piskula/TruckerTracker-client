package com.momosi.trucktrack.feature.signin.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.momosi.trucktrack.feature.myissues.api.MyIssuesNavKey
import com.momosi.trucktrack.feature.signin.api.SignInNavKey
import com.momosi.trucktrack.feature.signin.impl.SignInScreen
import com.momosi.trucktrack.core.navigation.Navigator

fun EntryProviderScope<NavKey>.signInEntries(navigator: Navigator) {
    entry<SignInNavKey> {
        SignInScreen(
            onNavigateToMyIssues = { navigator.navigate(MyIssuesNavKey) },
        )
    }

}
