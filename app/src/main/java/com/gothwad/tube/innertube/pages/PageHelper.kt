package com.gothwad.tube.innertube.pages

import com.gothwad.tube.innertube.models.MusicResponsiveListItemRenderer.FlexColumn
import com.gothwad.tube.innertube.models.Run

object PageHelper {
    fun extractRuns(
        columns: List<FlexColumn>,
        typeLike: String,
    ): List<Run> {
        val filteredRuns = mutableListOf<Run>()
        for (column in columns) {
            val runs =
                column.musicResponsiveListItemFlexColumnRenderer.text?.runs
                    ?: continue

            for (run in runs) {
                val typeStr =
                    run.navigationEndpoint
                        ?.watchEndpoint
                        ?.watchEndpointMusicSupportedConfigs
                        ?.watchEndpointMusicConfig
                        ?.musicVideoType
                        ?: run.navigationEndpoint
                            ?.browseEndpoint
                            ?.browseEndpointContextSupportedConfigs
                            ?.browseEndpointContextMusicConfig
                            ?.pageType
                        ?: continue

                if (typeLike in typeStr) {
                    filteredRuns.add(run)
                }
            }
        }
        return filteredRuns
    }
}
