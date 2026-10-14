package com.gothwad.tube.ui.tv.input

import android.view.KeyEvent
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TvShortsKeyMapperTest {
    @Test
    fun `dpad and media next previous move through the feed`() {
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_DPAD_UP)).isEqualTo(TvShortsAction.NEXT)
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_MEDIA_NEXT)).isEqualTo(TvShortsAction.NEXT)
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_DPAD_DOWN)).isEqualTo(TvShortsAction.PREVIOUS)
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_MEDIA_PREVIOUS)).isEqualTo(TvShortsAction.PREVIOUS)
    }

    @Test
    fun `playback and hardware seek keys map to the active short`() {
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)).isEqualTo(TvShortsAction.TOGGLE_PLAYBACK)
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_HEADSETHOOK)).isEqualTo(TvShortsAction.TOGGLE_PLAYBACK)
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_MEDIA_PLAY)).isEqualTo(TvShortsAction.PLAY)
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_MEDIA_PAUSE)).isEqualTo(TvShortsAction.PAUSE)
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_MEDIA_STOP)).isEqualTo(TvShortsAction.PAUSE)
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_MEDIA_REWIND)).isEqualTo(TvShortsAction.SEEK_BACK)
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_MEDIA_FAST_FORWARD)).isEqualTo(TvShortsAction.SEEK_FORWARD)
    }

    @Test
    fun `selection keys remain available to focused controls`() {
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_DPAD_CENTER)).isNull()
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_ENTER)).isNull()
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_DPAD_LEFT)).isNull()
        assertThat(TvShortsKeyMapper.map(KeyEvent.KEYCODE_DPAD_RIGHT)).isNull()
    }
}
