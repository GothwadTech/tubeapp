package com.gothwad.tube.ui.tv.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TvDestinationTest {
    @Test
    fun `primary destinations keep a stable order`() {
        assertThat(TvDestination.primary).containsExactly(
            TvDestination.HOME,
            TvDestination.MUSIC,
            TvDestination.SUBSCRIPTIONS,
            TvDestination.SEARCH,
            TvDestination.LIBRARY,
            TvDestination.SETTINGS,
        ).inOrder()
    }

    @Test
    fun `unfinished shorts tab stays hidden from primary navigation`() {
        assertThat(TvDestination.primary).doesNotContain(TvDestination.SHORTS)
    }

    @Test
    fun `shorts query route still selects the shorts tab`() {
        assertThat(TvDestination.matchesRoute(TvDestination.SHORTS, "shorts?shortsSource=saved%3Aid"))
            .isTrue()
        assertThat(TvDestination.fromRoute("shorts?shortsSource=saved%3Aid")).isEqualTo(TvDestination.SHORTS)
    }

    @Test
    fun `route lookup falls back to home`() {
        assertThat(TvDestination.fromRoute("search")).isEqualTo(TvDestination.SEARCH)
        assertThat(TvDestination.fromRoute("missing")).isEqualTo(TvDestination.HOME)
        assertThat(TvDestination.fromRoute(null)).isEqualTo(TvDestination.HOME)
    }
}
