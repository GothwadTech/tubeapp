/*
 * Copyright (C) 2025-2026 Flow | A-EDev
 * Copyright (C) 2026 Gothwad Tech (PlusTube)
 *
 * This file is part of PlusTube (https://github.com/GothwadTech/tubeapp),
 * a modified version of Flow (https://github.com/A-EDev/Flow).
 */

package com.gothwad.tube.ui.components.shared

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape

/**
 * Every artist portrait wears this shape, so an artist is recognisable as
 * one before the label is read. Albums and playlists keep the theme's rounded rectangles.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun flowArtistShape(): Shape = MaterialShapes.Cookie9Sided.toShape()

/**
 * The shape behind a shuffle or radio action, distinct from both artists and collections.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun flowActionShape(): Shape = MaterialShapes.Cookie12Sided.toShape()
