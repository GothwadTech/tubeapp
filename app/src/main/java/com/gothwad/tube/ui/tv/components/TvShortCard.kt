package com.gothwad.tube.ui.tv.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SlowMotionVideo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gothwad.tube.R
import com.gothwad.tube.data.model.Video
import com.gothwad.tube.ui.components.shared.VideoThumbnailImage
import com.gothwad.tube.ui.tv.focus.rememberTvFocusState
import com.gothwad.tube.ui.tv.focus.tvFocusScale
import com.gothwad.tube.ui.tv.theme.LocalTvDimens
import com.gothwad.tube.utils.formatTimeAgo
import com.gothwad.tube.utils.formatViewCount

/** Portrait ten-foot card for Shorts shelves and the saved-Shorts library. */
@Composable
fun TvShortCard(
    video: Video,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalTvDimens.current
    val focusState = rememberTvFocusState()
    val views = video.viewCount.takeIf { it > 0L }?.let(::formatViewCount)
    val viewsText = views?.let { stringResource(R.string.views_template, it) }
    val metadata =
        remember(video.channelName, video.uploadDate, viewsText) {
            listOfNotNull(
                video.channelName.takeIf { it.isNotBlank() },
                viewsText,
                formatTimeAgo(video.uploadDate).takeIf { it.isNotBlank() },
            ).joinToString(" • ")
        }

    Surface(
        onClick = onClick,
        modifier = modifier.tvFocusScale(focusState),
        color = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Surface(
                modifier = Modifier.fillMaxWidth().aspectRatio(9f / 16f),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border =
                    if (focusState.isFocused) {
                        BorderStroke(dimens.focusBorderWidth, MaterialTheme.colorScheme.onSurface)
                    } else {
                        null
                    },
            ) {
                Box(Modifier.fillMaxSize()) {
                    VideoThumbnailImage(
                        videoId = video.id,
                        model = video.thumbnailUrl,
                        contentDescription = video.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        portrait = true,
                    )
                    Surface(
                        modifier = Modifier.align(Alignment.TopStart).padding(8.dp),
                        shape = MaterialTheme.shapes.extraSmall,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    ) {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SlowMotionVideo,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                text = stringResource(R.string.shorts),
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = video.title.ifBlank { stringResource(R.string.shorts) },
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = metadata,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
