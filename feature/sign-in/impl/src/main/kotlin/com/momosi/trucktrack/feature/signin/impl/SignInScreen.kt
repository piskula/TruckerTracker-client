package com.momosi.trucktrack.feature.signin.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momosi.trucktrack.core.uilibrary.components.Button
import com.momosi.trucktrack.core.uilibrary.components.Icon
import com.momosi.trucktrack.core.uilibrary.components.Text
import com.momosi.trucktrack.core.uilibrary.icons.TruckTrackIcons
import com.momosi.trucktrack.core.uilibrary.theme.AppTheme
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import com.momosi.trucktrack.feature.signin.impl.R

private val HeroGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF1565C0), Color(0xFF1976D2)),
)

@Composable
internal fun SignInScreen(viewModel: SignInViewModel = hiltViewModel(), onNavigateToMyIssues: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                SignInEvent.NavigateToMyIssues -> onNavigateToMyIssues()
            }
        }
    }

    SignInContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun SignInContent(
    state: SignInState,
    onAction: (SignInAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val errorMessage = when (state) {
        is SignInState.Error.NoInternet -> stringResource(R.string.sign_in_error_no_internet)
        is SignInState.Error.NoActivity -> stringResource(R.string.sign_in_error_unable_to_start)
        is SignInState.Error.Generic -> stringResource(R.string.sign_in_error_failed)
        else -> null
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f)
                .background(HeroGradient),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = TruckTrackIcons.Truck,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color.White,
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.sign_in_app_name),
                    style = AppTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.sign_in_app_subtitle),
                    style = AppTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.75f),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
                .background(AppTheme.colors.surface)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                text = stringResource(R.string.sign_in_button),
                onClick = { onAction(SignInAction.SignInClick) },
                loading = state is SignInState.Loading,
                modifier = Modifier.fillMaxWidth(),
            )
            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.negative,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SignInIdlePreview() {
    TruckTrackTheme {
        SignInContent(
            state = SignInState.Idle,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun SignInErrorPreview() {
    TruckTrackTheme {
        SignInContent(
            state = SignInState.Error.NoInternet,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun SignInLoadingPreview() {
    TruckTrackTheme {
        SignInContent(
            state = SignInState.Loading,
            onAction = {},
        )
    }
}
