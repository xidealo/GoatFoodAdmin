package com.bunbeauty.shared.designsystem.compose.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdminTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors =
        if (isDarkTheme) {
            DarkAdminColors
        } else {
            LightAdminColors
        }
    val rememberedColors =
        remember {
            colors.copy()
        }.apply {
            update(colors)
        }
    val typography = AdminTypography()

    CompositionLocalProvider(
        LocalAdminColors provides rememberedColors,
        LocalAdminDimensions provides AdminDimensions(),
        LocalAdminTypography provides typography,
    ) {
        MaterialTheme(colorScheme = rememberedColors.toColorScheme()) {
            CompositionLocalProvider(
                LocalContentColor provides rememberedColors.main.onBackground,
                LocalTextStyle provides typography.bodyMedium,
                content = content,
            )
        }
    }
}

object AdminTheme {
    val colors: AdminColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAdminColors.current
    val typography: AdminTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAdminTypography.current
    val dimensions: AdminDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalAdminDimensions.current
}
