package com.bunbeauty.shared.feature.menulist.cropimage

import android.graphics.Color
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

object CropImageDefaults {
    private object MenuProductOptions {
        const val DEFAULT_X_RATIO = 1000
        const val DEFAULT_Y_RATIO = 667
        const val MIN_WIDTH = 1000
        const val MIN_HEIGHT = 667
    }

    private object AdditionOptions {
        const val DEFAULT_X_RATIO = 320
        const val DEFAULT_Y_RATIO = 320
        const val MIN_WIDTH = 320
        const val MIN_HEIGHT = 320
    }

    fun menuProductOptions(isDarkTheme: Boolean): CropImageOptions =
        themedOptions(
            isDarkTheme = isDarkTheme,
            aspectRatioX = MenuProductOptions.DEFAULT_X_RATIO,
            aspectRatioY = MenuProductOptions.DEFAULT_Y_RATIO,
            minWidth = MenuProductOptions.MIN_WIDTH,
            minHeight = MenuProductOptions.MIN_HEIGHT,
        )

    fun additionOptions(isDarkTheme: Boolean): CropImageOptions =
        themedOptions(
            isDarkTheme = isDarkTheme,
            aspectRatioX = AdditionOptions.DEFAULT_X_RATIO,
            aspectRatioY = AdditionOptions.DEFAULT_Y_RATIO,
            minWidth = AdditionOptions.MIN_WIDTH,
            minHeight = AdditionOptions.MIN_HEIGHT,
        )

    private fun themedOptions(
        isDarkTheme: Boolean,
        aspectRatioX: Int,
        aspectRatioY: Int,
        minWidth: Int,
        minHeight: Int,
    ): CropImageOptions {
        val chromeBackground = if (isDarkTheme) Color.parseColor("#161617") else Color.WHITE
        val chromeContent = if (isDarkTheme) Color.WHITE else Color.BLACK
        return CropImageOptions(
            imageSourceIncludeCamera = false,
            cropShape = CropImageView.CropShape.RECTANGLE,
            showProgressBar = false,
            autoZoomEnabled = false,
            aspectRatioX = aspectRatioX,
            aspectRatioY = aspectRatioY,
            minCropResultWidth = minWidth,
            minCropResultHeight = minHeight,
            fixAspectRatio = true,
            toolbarColor = chromeBackground,
            activityBackgroundColor = chromeBackground,
            activityMenuIconColor = chromeContent,
            activityMenuTextColor = chromeContent,
            toolbarBackButtonColor = chromeContent,
        )
    }
}
