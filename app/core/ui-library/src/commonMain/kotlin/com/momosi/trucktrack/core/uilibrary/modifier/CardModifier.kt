package com.momosi.trucktrack.core.uilibrary.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.momosi.trucktrack.core.uilibrary.theme.AppTheme
import com.momosi.trucktrack.core.uilibrary.theme.Shapes as AppShape

@Composable
fun Modifier.card(shape: Shape = AppShape.CardShape): Modifier = this
    .clip(shape)
    .background(AppTheme.colors.surface, shape)
    .border(1.dp, AppTheme.colors.outlineVariant, shape)
