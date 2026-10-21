package com.gothwad.tube.ui.tv.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SlowMotionVideo
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import com.gothwad.tube.R

/** Stable top-level destinations for Flow's TV interface. */
enum class TvDestination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    HOME("home", R.string.nav_home, Icons.Outlined.Home),
    SHORTS("shorts", R.string.nav_shorts, Icons.Outlined.SlowMotionVideo),
    MUSIC("music", R.string.nav_music, Icons.Outlined.MusicNote),
    SUBSCRIPTIONS("subscriptions", R.string.top_bar_subscriptions_title, Icons.Outlined.Subscriptions),
    SEARCH("search", R.string.search, Icons.Outlined.Search),
    LIBRARY("library", R.string.library, Icons.Outlined.VideoLibrary),
    SETTINGS("settings", R.string.settings, Icons.Outlined.Settings);

    companion object {
        // Temporarily hide the unfinished TV Shorts tab. Keep its route for existing entry points.
        val primary: List<TvDestination> = entries.filterNot { it == SHORTS }

        /** Routes may add query arguments (Shorts carries its queue source). */
        fun matchesRoute(
            destination: TvDestination,
            route: String?,
        ): Boolean = route == destination.route || route?.startsWith("${destination.route}?") == true

        fun isPrimaryRoute(route: String?): Boolean = entries.any { matchesRoute(it, route) }

        fun fromRoute(route: String?): TvDestination =
            entries.firstOrNull { matchesRoute(it, route) } ?: HOME
    }
}
