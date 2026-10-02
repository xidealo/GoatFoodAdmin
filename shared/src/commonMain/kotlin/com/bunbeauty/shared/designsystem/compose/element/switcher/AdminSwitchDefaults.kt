package com.bunbeauty.shared.designsystem.compose.element.switcher

import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme

object AdminSwitchDefaults {
    val switchColors: SwitchColors
        @Composable get() =
            SwitchDefaults.colors(
                checkedThumbColor = AdminTheme.colors.main.onPrimary,
                checkedTrackColor = AdminTheme.colors.main.primary,
                checkedBorderColor = AdminTheme.colors.main.primary,
                uncheckedThumbColor = AdminTheme.colors.main.onDisabled,
                uncheckedTrackColor = AdminTheme.colors.main.disabled,
                uncheckedBorderColor = AdminTheme.colors.main.onDisabled,
                disabledCheckedThumbColor = AdminTheme.colors.main.onDisabled,
                disabledCheckedTrackColor = AdminTheme.colors.main.disabled,
                disabledCheckedBorderColor = AdminTheme.colors.main.onDisabled,
                disabledUncheckedThumbColor = AdminTheme.colors.main.onDisabled,
                disabledUncheckedTrackColor = AdminTheme.colors.main.disabled,
                disabledUncheckedBorderColor = AdminTheme.colors.main.onDisabled,
            )
}
