package com.momosi.trucktrack.core.uilibrary.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momosi.trucktrack.core.uilibrary.theme.AppTheme
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.material3.TextButton as MaterialTextButton

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    MaterialButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minWidth = 200.dp),
        enabled = enabled && !loading,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.primary,
            contentColor = AppTheme.colors.onPrimary,
            disabledContainerColor = AppTheme.colors.primary,
            disabledContentColor = AppTheme.colors.onPrimary,
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            disabledElevation = 2.dp,
        ),
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = AppTheme.colors.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(text = text, style = AppTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialTextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(text = text, style = AppTheme.typography.labelLarge, color = AppTheme.colors.primary)
    }
}

@Preview
@Composable
private fun ButtonPreview() {
    TruckTrackTheme {
        Button(text = "Sign In", onClick = {})
    }
}

@Preview
@Composable
private fun ButtonLoadingPreview() {
    TruckTrackTheme {
        Button(text = "Sign In", onClick = {}, loading = true)
    }
}

@Preview
@Composable
private fun TextButtonPreview() {
    TruckTrackTheme {
        TextButton(text = "Reset", onClick = {})
    }
}
