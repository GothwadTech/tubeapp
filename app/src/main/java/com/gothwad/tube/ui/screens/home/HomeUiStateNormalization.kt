package com.gothwad.tube.ui.screens.home

import com.gothwad.tube.data.local.VideoHistoryEntry
import com.gothwad.tube.data.model.Video
import com.gothwad.tube.data.model.distinctByNonBlankKeyOrSelf

internal fun HomeUiState.withUniqueLazyContent(): HomeUiState {
    val uniqueVideos = videos.distinctByNonBlankKeyOrSelf(Video::id)
    val uniqueShorts = shorts.distinctByNonBlankKeyOrSelf(Video::id)
    val uniqueHistory = continueWatchingVideos.distinctByNonBlankKeyOrSelf(VideoHistoryEntry::videoId)
    return if (
        uniqueVideos === videos &&
        uniqueShorts === shorts &&
        uniqueHistory === continueWatchingVideos
    ) {
        this
    } else {
        copy(
            videos = uniqueVideos,
            shorts = uniqueShorts,
            continueWatchingVideos = uniqueHistory,
        )
    }
}
