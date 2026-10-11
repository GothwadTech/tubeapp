package com.gothwad.tube.innertube.pages

import com.gothwad.tube.innertube.models.YTItem

data class LibraryContinuationPage(
    val items: List<YTItem>,
    val continuation: String?,
)
