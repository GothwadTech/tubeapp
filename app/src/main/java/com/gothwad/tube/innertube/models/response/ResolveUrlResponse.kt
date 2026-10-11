package com.gothwad.tube.innertube.models.response

import com.gothwad.tube.innertube.models.NavigationEndpoint
import kotlinx.serialization.Serializable

@Serializable
data class ResolveUrlResponse(
    val endpoint: NavigationEndpoint? = null,
)
