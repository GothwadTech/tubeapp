package com.gothwad.tube.data.video

import com.gothwad.tube.data.model.Video
import com.gothwad.tube.innertube.models.response.PlayerResponse
import com.gothwad.tube.player.state.SubtitleOption
import com.gothwad.tube.player.stream.CaptionTrackResolver
import com.gothwad.tube.player.stream.InnerTubeVideoStreamExtractor
import com.gothwad.tube.player.stream.StreamProcessor
import com.gothwad.tube.player.stream.StreamSizeEstimator
import com.gothwad.tube.player.stream.durationMs
import com.gothwad.tube.player.stream.playableAudioFormats
import com.gothwad.tube.player.stream.playableVideoFormats
import com.gothwad.tube.utils.PerformanceDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

private const val EXTRACT_TIMEOUT_MS = 8_000L

/** Everything the download dialogs need to offer one video: its formats and each choice's size. */
data class VideoDownloadOptions(
    val video: Video,
    val videoFormats: List<PlayerResponse.StreamingData.Format>,
    val audioFormats: List<PlayerResponse.StreamingData.Format>,
    val streamSizes: Map<String, Long>,
    val subtitles: List<SubtitleOption> = emptyList(),
)

/**
 * Loads the formats for a video that is not playing, so a card can open the same download dialog
 * the player and Shorts open, with the same quality, codec and style settings applied.
 */
class VideoDownloadOptionsLoader
    @Inject
    constructor() {
        suspend fun load(video: Video): VideoDownloadOptions? =
            withContext(PerformanceDispatcher.networkIO) {
                val result =
                    withTimeoutOrNull(EXTRACT_TIMEOUT_MS) {
                        runCatching { InnerTubeVideoStreamExtractor.extract(video.id) }.getOrNull()
                    } ?: return@withContext null
                val videoFormats = result.playableVideoFormats()
                if (videoFormats.isEmpty()) return@withContext null
                val audioFormats = result.playableAudioFormats()
                VideoDownloadOptions(
                    video = video,
                    videoFormats = videoFormats,
                    audioFormats = audioFormats,
                    streamSizes = StreamSizeEstimator.fromInnerTubeFormats(videoFormats, audioFormats, result.durationMs() ?: 0L),
                    subtitles =
                        StreamProcessor.toSubtitleOptions(
                            StreamProcessor.processCaptions(CaptionTrackResolver.resolve(result.playerResponse)),
                        ),
                )
            }
    }
