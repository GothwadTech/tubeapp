package com.gothwad.tube.innertube.pages

import com.gothwad.tube.innertube.models.SongItem

data class PlaylistContinuationPage(
    val songs: List<SongItem>,
    val continuation: String?,
)
