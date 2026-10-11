/*
 * Copyright (C) 2025-2026 Flow | A-EDev
 * Copyright (C) 2026 Gothwad Tech (PlusTube)
 *
 * This file is part of PlusTube (https://github.com/GothwadTech/tubeapp),
 * a modified version of Flow (https://github.com/A-EDev/Flow).
 */

package com.gothwad.tube.ui.components.music.common

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Mood buttons colour themselves by position, so a row of them never repeats a container until
 * all three have been used.
 */
class MusicMoodToneTest {
    @Test
    fun moodTonesCycleThroughTheThreeContainers() {
        assertThat(MusicMoodTone.forIndex(0)).isEqualTo(MusicMoodTone.Primary)
        assertThat(MusicMoodTone.forIndex(1)).isEqualTo(MusicMoodTone.Secondary)
        assertThat(MusicMoodTone.forIndex(2)).isEqualTo(MusicMoodTone.Tertiary)
        assertThat(MusicMoodTone.forIndex(3)).isEqualTo(MusicMoodTone.Primary)
    }
}
