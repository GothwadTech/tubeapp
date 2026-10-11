package com.gothwad.tube.discord

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DiscordFossIsolationTest {
    @Test
    fun `foss classpath excludes functional Discord implementation`() {
        val forbiddenClasses = listOf(
            "com.gothwad.tube.discord.DiscordTokenStore",
            "com.gothwad.tube.discord.DiscordAuthTokens",
            "com.gothwad.tube.discord.DiscordPlaybackSource",
            "com.gothwad.tube.discord.DiscordPresenceCoordinator",
            "com.gothwad.tube.discord.KizzyDiscordPresenceTransport",
            "com.gothwad.tube.discord.KizzyGatewayProtocol",
        )

        forbiddenClasses.forEach { className ->
            assertThat(runCatching { Class.forName(className) }.isFailure).isTrue()
        }
    }

    @Test
    fun `foss runtime reports Discord unavailable`() {
        assertThat(DiscordPresenceRuntime.settingsState.value.isAvailable).isFalse()
        assertThat(DiscordPresenceRuntime.settingsState.value.isEnabled).isFalse()
        assertThat(DiscordPresenceRuntime.settingsState.value.summary)
            .isEqualTo(DiscordSettingsSummary.UNAVAILABLE)
    }
}
