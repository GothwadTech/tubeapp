package com.gothwad.tube.innertube.models.body

import com.gothwad.tube.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class ReelBody(
    val context: Context,
    val sequenceParams: String,
)
