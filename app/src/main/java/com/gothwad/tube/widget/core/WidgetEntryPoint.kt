package com.gothwad.tube.widget.core

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import com.gothwad.tube.widget.core.refresh.WidgetContentSync
import com.gothwad.tube.widget.downloads.DownloadsSource
import com.gothwad.tube.widget.onrepeat.OnRepeatSource
import com.gothwad.tube.widget.playlist.PlaylistWidgetSource
import com.gothwad.tube.widget.recent.RecentlyPlayedSource
import com.gothwad.tube.widget.week.WeekSource

/**
 * Glance widgets can't use constructor injection (the framework instantiates them),
 * so Hilt singletons are reached through this entry point.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun recentlyPlayedSource(): RecentlyPlayedSource

    fun downloadsSource(): DownloadsSource

    fun onRepeatSource(): OnRepeatSource

    fun playlistWidgetSource(): PlaylistWidgetSource

    fun weekSource(): WeekSource

    fun widgetContentSync(): WidgetContentSync
}

fun widgetEntryPoint(context: Context): WidgetEntryPoint =
    EntryPointAccessors.fromApplication(context.applicationContext, WidgetEntryPoint::class.java)
