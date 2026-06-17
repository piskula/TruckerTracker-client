package com.momosi.trucktrack.ui

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.momosi.trucktrack.core.navigation.Navigator
import com.momosi.trucktrack.core.navigation.rememberNavigationState
import com.momosi.trucktrack.core.navigation.toEntries
import com.momosi.trucktrack.core.uilibrary.modifier.LocalSharedTransitionScope
import com.momosi.trucktrack.feature.issues.api.IssuesNavKey
import com.momosi.trucktrack.feature.issues.impl.navigation.issuesEntries
import com.momosi.trucktrack.feature.profile.impl.navigation.profileEntries
import com.momosi.trucktrack.feature.signin.api.SignInNavKey
import com.momosi.trucktrack.feature.signin.impl.navigation.signInEntries
import com.momosi.trucktrack.user.model.AuthenticationState

@Composable
internal fun TruckTrackApp(viewModel: TruckTrackViewModel) {
    val startKey: NavKey = remember {
        when (viewModel.authenticationState.value) {
            AuthenticationState.Authorized -> IssuesNavKey
            AuthenticationState.Guest -> SignInNavKey
        }
    }

    val navigationState = rememberNavigationState(startKey = startKey)
    val navigator = remember { Navigator(navigationState) }


    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            Scaffold(modifier = Modifier.fillMaxSize().imePadding()) { innerPadding ->
                NavDisplay(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    entries = navigationState.toEntries(entryProvider {
                        signInEntries(navigator)
                        issuesEntries(navigator)
                        profileEntries(navigator)
                    }),
                    onBack = navigator::goBack,
                    sharedTransitionScope = this,
                )
            }
        }
    }
}
