package com.gothwad.tube.ui.tv.screens.settings

import com.gothwad.tube.ui.theme.ThemeMode
import com.gothwad.tube.ui.theme.ThemeVariant
import com.gothwad.tube.ui.theme.canonicalFamily
import com.gothwad.tube.ui.theme.defaultVariant

/** Classic TV options are presets of the same family, not separate stored families. */
internal fun tvThemePresetVariant(mode: ThemeMode): ThemeVariant? =
    when (mode) {
        ThemeMode.LIGHT, ThemeMode.DARK, ThemeMode.OLED -> mode.defaultVariant()
        else -> null // Preserve the chosen variant when switching other palette families.
    }

internal fun isTvThemeSelected(
    option: ThemeMode,
    storedMode: ThemeMode,
    storedVariant: ThemeVariant,
): Boolean =
    option.canonicalFamily() == storedMode.canonicalFamily() &&
        (tvThemePresetVariant(option)?.let { it == storedVariant } ?: true)
