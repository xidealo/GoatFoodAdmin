package com.bunbeauty.shared.designsystem.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme


internal fun AdminColors.toColorScheme(): ColorScheme {
    val base = if (isLight) lightColorScheme() else darkColorScheme()
    return base.copy(
        primary = main.primary,
        onPrimary = main.onPrimary,
        secondary = main.secondary,
        onSecondary = main.onSecondary,
        background = main.background,
        onBackground = main.onBackground,
        surface = main.surface,
        onSurface = main.onSurface,
        surfaceVariant = main.surfaceVariant,
        onSurfaceVariant = main.onSurfaceVariant,
        error = main.error,
        onError = main.onError,
        outline = main.stroke,
        outlineVariant = main.strokeVariant,
        // AlertDialog uses surfaceContainerHigh, ModalBottomSheet uses surfaceContainerLow
        surfaceContainer = main.surface,
        surfaceContainerHigh = main.surface,
        surfaceContainerHighest = main.surface,
        surfaceContainerLow = main.surface,
        surfaceContainerLowest = main.surface,
    )
}
