package com.gothwad.tube.ui.tv.input

import android.view.KeyEvent

/** Remote/media-key intents for the TV Shorts feed. */
enum class TvShortsAction {
    NEXT,
    PREVIOUS,
    TOGGLE_PLAYBACK,
    PLAY,
    PAUSE,
    SEEK_BACK,
    SEEK_FORWARD,
}

/**
 * Pure keyCode-to-action mapping. Center/Enter are intentionally left to the focused Material 3
 * control so Like, Save, Comments, and the video surface all keep their normal click behaviour.
 */
object TvShortsKeyMapper {
    fun map(keyCode: Int): TvShortsAction? =
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_MEDIA_NEXT,
            -> TvShortsAction.NEXT

            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_MEDIA_PREVIOUS,
            -> TvShortsAction.PREVIOUS

            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
            KeyEvent.KEYCODE_HEADSETHOOK,
            -> TvShortsAction.TOGGLE_PLAYBACK

            KeyEvent.KEYCODE_MEDIA_PLAY -> TvShortsAction.PLAY
            KeyEvent.KEYCODE_MEDIA_PAUSE,
            KeyEvent.KEYCODE_MEDIA_STOP,
            -> TvShortsAction.PAUSE

            KeyEvent.KEYCODE_MEDIA_REWIND,
            -> TvShortsAction.SEEK_BACK

            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
            -> TvShortsAction.SEEK_FORWARD

            else -> null
        }
}
