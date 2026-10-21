package com.gothwad.tube.ui.tv.screens.settings

import com.gothwad.tube.ui.theme.ThemeMode
import com.gothwad.tube.ui.theme.ThemeVariant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TvThemeSelectionTest {
    @Test
    fun `classic presets apply the corresponding variant`() {
        assertEquals(ThemeVariant.LIGHT, tvThemePresetVariant(ThemeMode.LIGHT))
        assertEquals(ThemeVariant.DARK, tvThemePresetVariant(ThemeMode.DARK))
        assertEquals(ThemeVariant.AMOLED, tvThemePresetVariant(ThemeMode.OLED))
    }

    @Test
    fun `only the matching classic preset is selected after canonical storage`() {
        val presets = listOf(ThemeMode.LIGHT, ThemeMode.DARK, ThemeMode.OLED)
        presets.forEach { chosen ->
            presets.forEach { option ->
                assertEquals(
                    chosen == option,
                    isTvThemeSelected(option, ThemeMode.DARK, tvThemePresetVariant(chosen)!!),
                )
            }
        }
    }

    @Test
    fun `other families preserve the variant and never select classic presets`() {
        listOf(ThemeMode.SYSTEM, ThemeMode.MATERIAL_YOU, ThemeMode.MONOCHROME).forEach { mode ->
            assertNull(tvThemePresetVariant(mode))
            ThemeVariant.entries.forEach { variant ->
                assertTrue(isTvThemeSelected(mode, mode, variant))
                assertFalse(isTvThemeSelected(ThemeMode.LIGHT, mode, variant))
                assertFalse(isTvThemeSelected(ThemeMode.OLED, mode, variant))
            }
        }
    }
}
