package com.bunbeauty.shared.designsystem.compose.element.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme

object AdminCardDefaults {
    val cardColors: CardColors
        @Composable get() =
            CardDefaults.cardColors(
                containerColor = AdminTheme.colors.main.surface,
                contentColor = AdminTheme.colors.main.onSurface,
                disabledContainerColor = AdminTheme.colors.main.surface,
                disabledContentColor = AdminTheme.colors.main.onSurface,
            )
    val cardVariantColors: CardColors
        @Composable get() =
            CardDefaults.cardColors(
                containerColor = AdminTheme.colors.main.surfaceVariant,
                contentColor = AdminTheme.colors.main.onSurface,
                disabledContainerColor = AdminTheme.colors.main.surfaceVariant,
                disabledContentColor = AdminTheme.colors.main.onSurface,
            )

    val cardPositiveColors: CardColors
        @Composable get() =
            CardDefaults.cardColors(
                containerColor = AdminTheme.colors.status.positive,
                contentColor = AdminTheme.colors.status.onStatus,
            )

    val cardBorder: BorderStroke
        @Composable get() =
            BorderStroke(
                width = 1.dp,
                color = AdminTheme.colors.main.strokeVariant,
            )

    val warningCardStatusColors: CardColors
        @Composable get() =
            CardDefaults.cardColors(
                containerColor = AdminTheme.colors.status.warning,
                contentColor = AdminTheme.colors.status.onStatus,
            )

    val cardShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(AdminTheme.dimensions.cardRadius)

    val smallCardShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(AdminTheme.dimensions.smallCardRadius)

    val noCornerCardShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(0.dp)

    @Composable
    fun getCardElevation(elevated: Boolean): CardElevation =
        if (elevated) {
            CardDefaults.cardElevation(
                defaultElevation = AdminTheme.dimensions.cardElevation,
                disabledElevation = AdminTheme.dimensions.cardElevation,
            )
        } else {
            CardDefaults.cardElevation(
                defaultElevation = 0.dp,
                disabledElevation = 0.dp,
            )
        }
}
