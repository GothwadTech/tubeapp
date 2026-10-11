package com.gothwad.tube.ui.screens.settings.about

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// Brand marks have no Material symbol. The fill is a placeholder: Icon tints the path.
internal val IconX: ImageVector by lazy {
    ImageVector
        .Builder(
            name = "X",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black), pathFillType = PathFillType.NonZero) {
                moveTo(18.901f, 1.153f)
                horizontalLineToRelative(3.68f)
                lineToRelative(-8.04f, 9.19f)
                lineTo(24f, 22.846f)
                horizontalLineToRelative(-7.406f)
                lineToRelative(-5.8f, -7.584f)
                lineToRelative(-6.638f, 7.584f)
                horizontalLineTo(0.474f)
                lineToRelative(8.6f, -9.83f)
                lineTo(0f, 1.154f)
                horizontalLineToRelative(7.594f)
                lineToRelative(5.243f, 6.932f)
                close()
                moveTo(17.61f, 20.644f)
                horizontalLineToRelative(2.039f)
                lineTo(6.486f, 3.24f)
                horizontalLineTo(4.298f)
                close()
            }
        }.build()
}
