package com.gothwad.tube.ui.tv.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gothwad.tube.R
import com.gothwad.tube.data.model.Comment
import com.gothwad.tube.data.model.ShortVideo
import com.gothwad.tube.ui.components.shared.ChannelAvatarImage
import com.gothwad.tube.ui.screens.shorts.ShortsViewModel
import com.gothwad.tube.ui.tv.components.TvButton
import com.gothwad.tube.ui.tv.components.TvCard
import com.gothwad.tube.ui.tv.components.TvSidePanel
import com.gothwad.tube.ui.tv.focus.tvInitialFocus

internal enum class TvShortsPanel {
    COMMENTS,
    DESCRIPTION,
    MORE,
}

@Composable
internal fun BoxScope.TvShortsPanelHost(
    activePanel: TvShortsPanel?,
    short: ShortVideo,
    viewModel: ShortsViewModel,
    onClose: () -> Unit,
) {
    val title =
        when (activePanel) {
            TvShortsPanel.COMMENTS -> stringResource(R.string.comments)
            TvShortsPanel.DESCRIPTION -> stringResource(R.string.description)
            TvShortsPanel.MORE -> stringResource(R.string.more_options)
            null -> ""
        }

    TvSidePanel(
        visible = activePanel != null,
        title = title,
        onClose = onClose,
    ) {
        when (activePanel) {
            TvShortsPanel.COMMENTS -> TvShortCommentsPanel(short = short, viewModel = viewModel)
            TvShortsPanel.DESCRIPTION -> TvShortDescriptionPanel(short = short)
            TvShortsPanel.MORE -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TvButton(
                        text = stringResource(R.string.action_want_more),
                        icon = Icons.Outlined.ThumbUp,
                        onClick = {
                            viewModel.wantMoreLikeThis(short)
                            onClose()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    TvButton(
                        text = stringResource(R.string.not_interested),
                        icon = Icons.Outlined.ThumbDown,
                        onClick = {
                            viewModel.notInterested(short)
                            onClose()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    TvButton(
                        text = stringResource(R.string.taste_channel_block),
                        icon = Icons.Outlined.Block,
                        onClick = {
                            viewModel.blockChannel(short)
                            onClose()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            null -> Unit
        }
    }
}

@Composable
private fun TvShortCommentsPanel(
    short: ShortVideo,
    viewModel: ShortsViewModel,
) {
    val comments by viewModel.commentsState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingComments.collectAsStateWithLifecycle()
    val hasMore by viewModel.hasMoreComments.collectAsStateWithLifecycle()
    val isLoadingMore by viewModel.isLoadingMoreComments.collectAsStateWithLifecycle()
    var expandedIds by remember(short.id) { mutableStateOf(emptySet<String>()) }

    LaunchedEffect(short.id) {
        viewModel.loadComments(short.id)
    }

    when {
        isLoading && comments.isEmpty() -> {
            Box(Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        comments.isEmpty() -> {
            Text(
                text = stringResource(R.string.no_comments_yet),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(comments, key = Comment::id) { comment ->
                    val expanded = comment.id in expandedIds
                    val likeCountText = comment.likeCount.takeIf { it > 0 }?.toString()
                    val replyCountText =
                        comment.replyCount.takeIf { it > 0 }?.let { count ->
                            if (expanded) {
                                stringResource(R.string.hide_replies)
                            } else {
                                pluralStringResource(R.plurals.view_replies_template, count, count)
                            }
                        }
                    val socialMeta = listOfNotNull(likeCountText, replyCountText).joinToString("  •  ")
                    TvCard(
                        onClick = {
                            if (comment.replyCount > 0 || comment.replies.isNotEmpty()) {
                                if (expanded) {
                                    expandedIds = expandedIds - comment.id
                                } else {
                                    expandedIds = expandedIds + comment.id
                                    if (comment.replies.isEmpty()) viewModel.loadCommentReplies(comment)
                                }
                            }
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .then(if (comment.id == comments.first().id) Modifier.tvInitialFocus(short.id) else Modifier),
                        selected = expanded,
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                ChannelAvatarImage(
                                    url = comment.authorThumbnail,
                                    contentDescription = comment.author,
                                    modifier = Modifier.size(40.dp).clip(CircleShape),
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = listOf(comment.author, comment.publishedTime).filter(String::isNotBlank).joinToString(" • "),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(text = comment.text, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            if (socialMeta.isNotBlank()) {
                                Text(
                                    text = socialMeta,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            if (expanded) {
                                comment.replies.forEach { reply ->
                                    Column(
                                        modifier = Modifier.padding(start = 44.dp),
                                        verticalArrangement = Arrangement.spacedBy(2.dp),
                                    ) {
                                        Text(
                                            text = reply.author,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        Text(text = reply.text, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                                if (comment.replies.isEmpty() && comment.replyCount > 0) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
                if (hasMore) {
                    item(key = "load-more-comments") {
                        TvButton(
                            text =
                                stringResource(
                                    if (isLoadingMore) R.string.loading_more else R.string.tv_shorts_load_more_comments,
                                ),
                            onClick = { if (!isLoadingMore) viewModel.loadMoreComments(short.id) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TvShortDescriptionPanel(short: ShortVideo) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = short.title.ifBlank { stringResource(R.string.shorts) },
            style = MaterialTheme.typography.titleLarge,
        )
        if (short.description.isBlank()) {
            Text(
                text = stringResource(R.string.loading_ellipsis),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            Text(
                text = short.description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
