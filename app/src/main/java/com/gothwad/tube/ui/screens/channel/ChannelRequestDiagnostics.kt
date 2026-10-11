package com.gothwad.tube.ui.screens.channel

import android.os.Build
import com.gothwad.tube.BuildConfig
import com.gothwad.tube.innertube.YouTube
import com.gothwad.tube.innertube.models.YouTubeClient

internal fun buildChannelRequestErrorLog(
    operation: String,
    channelId: String,
    error: Throwable,
    query: String? = null,
): String {
    val locale = YouTube.locale
    return buildString {
        appendLine("PLUSTUBE CHANNEL REQUEST ERROR")
        appendLine("Operation: $operation")
        appendLine("App: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
        appendLine("Device: ${Build.MANUFACTURER} ${Build.MODEL}")
        appendLine("Android: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})")
        appendLine("WEB client: ${YouTubeClient.WEB.clientVersion}")
        appendLine("Locale: hl=${locale.hl}, gl=${locale.gl}")
        appendLine("Visitor data present: ${!YouTube.visitorData.isNullOrBlank()}")
        appendLine("Channel: $channelId")
        query?.let { appendLine("Query: $it") }
        appendLine()
        append(error.stackTraceToString())
    }
}
