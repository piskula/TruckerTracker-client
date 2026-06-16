package com.momosi.trucktrack.feature.myissues.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momosi.trucktrack.core.uilibrary.components.Text
import com.momosi.trucktrack.core.uilibrary.theme.AppTheme
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import com.momosi.trucktrack.user.model.User
import com.momosi.trucktrack.user.model.UserRole

@Composable
internal fun MyIssuesScreen(
    viewModel: MyIssuesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MyIssuesContent(state = state)
}

@Composable
private fun MyIssuesContent(
    state: MyIssuesState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "My Issues",
                style = AppTheme.typography.headlineMedium,
            )
            state.user?.let { user ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = listOfNotNull(
                        user.name.takeIf { it.isNotEmpty() },
                        user.email.takeIf { it.isNotEmpty() },
                        user.role?.name,
                    ).joinToString(" • "),
                    style = AppTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MyIssuesContentPreview() {
    TruckTrackTheme {
        MyIssuesContent(
            state = MyIssuesState(
                user = User(
                    name = "John Doe",
                    email = "john.doe@example.com",
                    role = UserRole.Driver,
                ),
            ),
        )
    }
}

@Preview
@Composable
private fun MyIssuesContentLoadingPreview() {
    TruckTrackTheme {
        MyIssuesContent(state = MyIssuesState())
    }
}

