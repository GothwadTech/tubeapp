package com.gothwad.tube.ui.tv.navigation

import android.net.Uri
import com.gothwad.tube.data.shorts.queue.ShortsQueueSource
import com.gothwad.tube.ui.screens.music.collection.MUSIC_COLLECTION_ARG as COLLECTION_ROUTE_ARG

/** Detail routes layered over the top-level [TvDestination] tabs. */
object TvRoutes {
    const val CHANNEL_ARG = "channelRef"
    const val CHANNEL = "channel?ref={$CHANNEL_ARG}"

    const val PLAYLIST_ARG = "playlistId"
    const val PLAYLIST = "playlist/{$PLAYLIST_ARG}"

    const val MUSIC_COLLECTION_ARG = COLLECTION_ROUTE_ARG
    const val MUSIC_COLLECTION = "musicCollection/{$MUSIC_COLLECTION_ARG}"

    const val MUSIC_ARTIST_ARG = "artistChannelId"
    const val MUSIC_ARTIST = "musicArtist/{$MUSIC_ARTIST_ARG}"

    const val SHORTS_SOURCE_ARG = "shortsSource"
    const val SHORTS = "shorts?$SHORTS_SOURCE_ARG={$SHORTS_SOURCE_ARG}"

    const val SYNC = "sync"
    const val REMOTE_GUIDE = "remoteGuide"

    /** [channelRef] is a full channel URL (preferred) or a bare channel id. */
    fun channel(channelRef: String): String = "channel?ref=${Uri.encode(channelRef)}"

    fun playlist(playlistId: String): String = "playlist/${Uri.encode(playlistId)}"

    fun musicCollection(collectionId: String): String = "musicCollection/${Uri.encode(collectionId)}"

    fun musicArtist(channelId: String): String = "musicArtist/${Uri.encode(channelId)}"

    /** Feed is the top-level Shorts tab; other sources retain their queue descriptor in the route. */
    fun shorts(source: ShortsQueueSource): String =
        if (source == ShortsQueueSource.Feed) {
            TvDestination.SHORTS.route
        } else {
            "${TvDestination.SHORTS.route}?$SHORTS_SOURCE_ARG=${Uri.encode(source.encode())}"
        }
}
