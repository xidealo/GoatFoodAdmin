package com.bunbeauty.shared.designsystem.compose.theme

import androidx.compose.ui.graphics.Color
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Black100
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Black2
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Black3
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Black50
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Blue1
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Blue2
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Cream
import com.bunbeauty.shared.designsystem.compose.theme.Colors.DarkGrey
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Green
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Grey1
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Grey2
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Grey3
import com.bunbeauty.shared.designsystem.compose.theme.Colors.LightGreen
import com.bunbeauty.shared.designsystem.compose.theme.Colors.LightRed
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Purple
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Red
import com.bunbeauty.shared.designsystem.compose.theme.Colors.White
import com.bunbeauty.shared.designsystem.compose.theme.Colors.Yellow

object ColorDefaults {
    fun lightMainColors(
        primary: Color,
        surfaceVariant: Color,
        strokeVariant: Color,
    ): MainColors =
        MainColors(
            primary = primary,
            disabled = Grey1,
            secondary = White,
            background = Cream,
            surface = White,
            surfaceVariant = surfaceVariant,
            error = Red,
            onPrimary = White,
            onDisabled = Grey3,
            onSecondary = Grey3,
            onBackground = Colors.Black1,
            onSurface = Colors.Black1,
            onSurfaceVariant = Grey2,
            onError = White,
            stroke = Cream,
            strokeVariant = strokeVariant,
        )

    fun darkMainColors(
        primary: Color,
        surface: Color = Black3,
    ): MainColors =
        MainColors(
            primary = primary,
            disabled = Black100,
            secondary = Black3,
            background = Black2,
            surface = surface,
            surfaceVariant = Black100,
            error = Red,
            onPrimary = White,
            onDisabled = Grey3,
            onSecondary = Grey3,
            onBackground = White,
            onSurface = White,
            onSurfaceVariant = Grey2,
            onError = White,
            stroke = Black50,
            strokeVariant = Black50,
        )

    fun orderColors(): OrderColors =
        OrderColors(
            notAccepted = Purple,
            accepted = Blue2,
            preparing = LightRed,
            sentOut = Yellow,
            done = LightGreen,
            delivered = Green,
            canceled = DarkGrey,
            onOrder = White,
        )

    fun statusColors(): StatusColors =
        StatusColors(
            positive = Green,
            warning = Yellow,
            negative = LightRed,
            info = Blue1,
            onStatus = White,
        )
}
