package com.momosi.trucktrack.feature.profile.impl

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momosi.trucktrack.core.uilibrary.components.Button
import com.momosi.trucktrack.core.uilibrary.components.Icon
import com.momosi.trucktrack.core.uilibrary.components.Text
import com.momosi.trucktrack.core.uilibrary.components.Toolbar
import com.momosi.trucktrack.core.uilibrary.icons.TruckTrackIcons
import com.momosi.trucktrack.core.uilibrary.theme.AppTheme
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import com.momosi.trucktrack.feature.profile.impl.resources.Res
import com.momosi.trucktrack.feature.profile.impl.resources.profile_role_driver
import com.momosi.trucktrack.feature.profile.impl.resources.profile_role_mechanic
import com.momosi.trucktrack.feature.profile.impl.resources.profile_sign_out
import com.momosi.trucktrack.feature.profile.impl.resources.profile_title
import com.momosi.trucktrack.user.model.User
import com.momosi.trucktrack.user.model.UserRole
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private val HeroGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF1565C0), Color(0xFF1976D2)),
)

@Composable
internal fun ProfileScreen(
    onNavigateToSignIn: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                ProfileEvent.NavigateToSignIn -> onNavigateToSignIn()
            }
        }
    }

    ProfileContent(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}

@Composable
private fun ProfileContent(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Toolbar(
            title = stringResource(Res.string.profile_title),
            onBack = onBack,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(HeroGradient),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = TruckTrackIcons.Person,
                        modifier = Modifier.size(56.dp),
                        tint = Color.White,
                    )
                }
                state.user?.let { user ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = user.name,
                        style = AppTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                    )
                    if (user.email.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = user.email,
                                style = AppTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.75f),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = user.roles.map { stringResource(it.labelRes()) }.joinToString(" · "),
                        style = AppTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f),
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.surface)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                text = stringResource(Res.string.profile_sign_out),
                onClick = { onAction(ProfileAction.SignOut) },
                loading = state.isSigningOut,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun UserRole.labelRes(): StringResource = when (this) {
    UserRole.Driver -> Res.string.profile_role_driver
    UserRole.Mechanic -> Res.string.profile_role_mechanic
}

@Preview
@Composable
private fun ProfileDriverPreview() {
    TruckTrackTheme {
        ProfileContent(
            state = ProfileState(
                user = User(id = "", name = "Michael Schumacher", email = "michael@example.com", roles = setOf(UserRole.Driver)),
            ),
            onAction = {},
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun ProfileMechanicPreview() {
    TruckTrackTheme {
        ProfileContent(
            state = ProfileState(
                user = User(id = "", name = "Mattia Binotto", email = "mattia@example.com", roles = setOf(UserRole.Mechanic)),
            ),
            onAction = {},
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun ProfileDualRolePreview() {
    TruckTrackTheme {
        ProfileContent(
            state = ProfileState(
                user = User(id = "", name = "Lewis Hamilton", email = "lewis@hamilton.com", roles = setOf(UserRole.Driver, UserRole.Mechanic)),
            ),
            onAction = {},
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun ProfileLoadingPreview() {
    TruckTrackTheme {
        ProfileContent(
            state = ProfileState(),
            onAction = {},
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun ProfileSigningOutPreview() {
    TruckTrackTheme {
        ProfileContent(
            state = ProfileState(
                user = User(id = "", name = "Michael Schumacher", email = "michael@example.com", roles = setOf(UserRole.Driver)),
                isSigningOut = true,
            ),
            onAction = {},
            onBack = {},
        )
    }
}
