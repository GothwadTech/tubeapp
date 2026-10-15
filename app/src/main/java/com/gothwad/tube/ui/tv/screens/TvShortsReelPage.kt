package com.gothwad.tube.ui.tv.screens

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.gothwad.tube.R
import com.gothwad.tube.data.model.ShortVideo
import com.gothwad.tube.player.shorts.ShortsPlayerPool
import com.gothwad.tube.ui.components.shared.ChannelAvatarImage
import com.gothwad.tube.ui.components.shared.rememberVideoShareAction
import com.gothwad.tube.ui.components.shorts.ShortsBufferingIndicator
import com.gothwad.tube.ui.components.shorts.ShortsReelAutoAdvanceState
import com.gothwad.tube.ui.components.shorts.ShortsReelPageState
import com.gothwad.tube.ui.components.shorts.ShortsReelPlaybackEffects
import com.gothwad.tube.ui.components.shorts.ShortsReelSessionState
import com.gothwad.tube.ui.components.shorts.ShortsReelSettings
import com.gothwad.tube.ui.components.shorts.ShortsReelPoster
import com.gothwad.tube.ui.screens.shorts.ShortsViewModel
import com.gothwad.tube.ui.theme.PlayerScrim
import com.gothwad.tube.ui.theme.PlayerScrimContent
import com.gothwad.tube.ui.theme.PlayerScrimPanel
import com.gothwad.tube.ui.tv.components.TvButton
import com.gothwad.tube.ui.tv.components.TvIconButton
import com.gothwad.tube.ui.tv.focus.rememberTvFocusState
import com.gothwad.tube.ui.tv.focus.tvFocusScale
import com.gothwad.tube.ui.tv.theme.LocalTvDimens
import com.gothwad.tube.utils.formatViewCount
import kotlinx.coroutines.launch

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
internal fun TvShortsReelPage(
    short: ShortVideo,
    isActive: Boolean,
    pageIndex: Int,
    pageNumber: Int,
    totalPages: Int,
    viewModel: ShortsViewModel,
    settings: ShortsReelSettings,
    activePanel: TvShortsPanel?,
    videoFocusRequester: FocusRequester?,
    firstActionFocusRequester: FocusRequester?,
    onBack: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenDescription: () -> Unit,
    onOpenMore: () -> Unit,
    onOpenChannel: () -> Unit,
    onPlayLinkedVideo: (ShortVideo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val playerPool = remember { ShortsPlayerPool.getInstance() }
    val pageState = remember(short.id) { ShortsReelPageState() }
    val sessionState = remember(short.id) { ShortsReelSessionState() }
    val autoAdvanceState = remember(short.id, isActive, settings.playbackMode, settings.autoScrollSeconds) { ShortsReelAutoAdvanceState() }
    val shareVideo = rememberVideoShareAction()

    val isLiked by remember(short.id) { viewModel.isVideoLikedState(short.id) }.collectAsStateWithLifecycle()
    val isSubscribed by remember(short.channelId) { viewModel.isChannelSubscribedState(short.channelId) }.collectAsStateWithLifecycle()
    val isSaved by remember(short.id) { viewModel.isShortSavedState(short.id) }.collectAsStateWithLifecycle()

    val playerView =
        remember(context, short.id) {
            PlayerView(context).apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
                setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                isFocusable = false
                isFocusableInTouchMode = false
            }
        }

    ShortsReelPlaybackEffects(
        playerPool = playerPool,
        playerView = playerView,
        short = short,
        pageIndex = pageIndex,
        isActive = isActive,
        settings = settings,
        pageState = pageState,
        sessionState = sessionState,
        autoAdvanceState = autoAdvanceState,
        sheetOpen = activePanel != null,
        viewModel = viewModel,
        onVideoEnded = onNext,
        onAttachedPlayerChange = {},
    )

    val isVideoFocused = remember(short.id) { mutableStateOf(false) }
    val isLikedCount = short.likeCount.takeIf { it > 0L }?.let(::formatViewCount)
    val commentsCount = short.commentCount.takeIf { it > 0L }?.let(::formatViewCount)
    val likeLabel = isLikedCount ?: stringResource(R.string.action_like)
    val commentLabel = commentsCount ?: stringResource(R.string.action_comments)
    val saveLabel = stringResource(if (isSaved) R.string.saved else R.string.action_save)
    val title = short.title.ifBlank { stringResource(R.string.shorts) }
    val views = short.viewCount.takeIf { it > 0L }?.let { stringResource(R.string.views_template, formatViewCount(it)) }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize().background(PlayerScrim),
    ) {
        val dimens = LocalTvDimens.current
        val frameHeight =
            minOf(
                maxHeight - dimens.overscanVertical * 2,
                maxWidth * 0.5f * (16f / 9f),
            ).coerceAtLeast(1.dp)
        val frameWidth = frameHeight * (9f / 16f)

        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = dimens.overscanHorizontal, vertical = dimens.overscanVertical),
            horizontalArrangement = Arrangement.spacedBy(dimens.itemSpacing * 2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                onClick = {
                    if (isActive) {
                        playerPool.togglePlayPause()
                        playerPool.ownedPlayer(pageIndex, short.id)?.let { pageState.isPlaying = it.isPlaying }
                    }
                },
                modifier =
                    Modifier
                        .width(frameWidth)
                        .height(frameHeight)
                        .then(videoFocusRequester?.let { Modifier.focusRequester(it) } ?: Modifier)
                        .focusProperties {
                            if (firstActionFocusRequester != null) right = firstActionFocusRequester
                        }
                        .onFocusChanged { isVideoFocused.value = it.isFocused },
                shape = MaterialTheme.shapes.large,
                color = PlayerScrim,
                contentColor = PlayerScrimContent,
                border =
                    if (isVideoFocused.value) {
                        BorderStroke(dimens.focusBorderWidth, MaterialTheme.colorScheme.onSurface)
                    } else {
                        null
                    },
            ) {
                Box(Modifier.fillMaxSize()) {
                    AndroidView(factory = { playerView }, modifier = Modifier.fillMaxSize())
                    ShortsReelPoster(
                        visible = !pageState.hasStartedPlaying && !pageState.isBuffering,
                        videoId = short.id,
                        thumbnailUrl = short.thumbnailUrl,
                    )
                    ShortsBufferingIndicator(
                        visible = pageState.isBuffering,
                        modifier = Modifier.align(Alignment.Center),
                    )
                    if (!pageState.isPlaying && !pageState.isBuffering) {
                        Surface(
                            modifier = Modifier.align(Alignment.Center).size(76.dp),
                            shape = CircleShape,
                            color = PlayerScrimPanel,
                            contentColor = PlayerScrimContent,
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = stringResource(R.string.cd_play),
                                    modifier = Modifier.size(48.dp),
                                )
                            }
                        }
                    }
                    if (pageState.duration > 0L) {
                        LinearProgressIndicator(
                            progress = {
                                (pageState.currentPosition.toFloat() / pageState.duration.toFloat()).coerceIn(0f, 1f)
                            },
                            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                        shape = CircleShape,
                        color = PlayerScrimPanel,
                        contentColor = PlayerScrimContent,
                    ) {
                        Text(
                            text = stringResource(R.string.tv_shorts_position_template, pageNumber, totalPages),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        TvIconButton(
                            icon = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            onClick = onBack,
                        )
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(R.string.shorts),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineMedium,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        TvIconButton(
                            icon = Icons.Outlined.SkipPrevious,
                            contentDescription = stringResource(R.string.previous_video),
                            onClick = onPrevious,
                            enabled = pageNumber > 1,
                        )
                        TvIconButton(
                            icon = Icons.Outlined.SkipNext,
                            contentDescription = stringResource(R.string.next_video),
                            onClick = onNext,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ChannelAvatarImage(
                            url = short.channelThumbnailUrl,
                            contentDescription = short.channelName,
                            modifier = Modifier.size(56.dp),
                        )
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = short.channelName.ifBlank { stringResource(R.string.channel_name_clip_label) },
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = listOfNotNull(views, short.uploadDate.takeIf(String::isNotBlank)).joinToString(" • "),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        if (short.channelId.isNotBlank()) {
                            TvButton(
                                text = stringResource(R.string.tv_shorts_open_channel),
                                onClick = onOpenChannel,
                            )
                            TvButton(
                                text = stringResource(if (isSubscribed) R.string.subscribed else R.string.subscribe),
                                onClick = {
                                    scope.launch {
                                        viewModel.toggleSubscription(short.channelId, short.channelName, short.channelThumbnailUrl)
                                    }
                                },
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().focusGroup(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TvShortAction(
                            icon = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                            label = likeLabel,
                            description = stringResource(R.string.action_like),
                            active = isLiked,
                            focusRequester = firstActionFocusRequester,
                            onClick = { scope.launch { viewModel.toggleLike(short) } },
                        )
                        TvShortAction(
                            icon = Icons.Outlined.ChatBubbleOutline,
                            label = commentLabel,
                            description = stringResource(R.string.action_comments),
                            onClick = onOpenComments,
                        )
                        TvShortAction(
                            icon = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            label = saveLabel,
                            description = stringResource(R.string.action_save),
                            active = isSaved,
                            onClick = { viewModel.toggleSaveShort(short) },
                        )
                        TvShortAction(
                            icon = Icons.Outlined.Share,
                            label = stringResource(R.string.share),
                            description = stringResource(R.string.action_share),
                            onClick = { shareVideo(short.id, short.title, true) },
                        )
                        TvShortAction(
                            icon = Icons.Outlined.MoreVert,
                            label = stringResource(R.string.more_options),
                            description = stringResource(R.string.more_options),
                            onClick = onOpenMore,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        TvButton(
                            text = stringResource(if (pageState.isPlaying) R.string.pause else R.string.play),
                            icon = if (pageState.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            onClick = {
                                playerPool.togglePlayPause()
                                playerPool.ownedPlayer(pageIndex, short.id)?.let { pageState.isPlaying = it.isPlaying }
                            },
                        )
                        TvButton(
                            text = stringResource(R.string.description),
                            onClick = onOpenDescription,
                        )
                    }

                    short.linkedVideo?.let { linked ->
                        TvButton(
                            text = linked.title ?: stringResource(R.string.shorts_linked_video),
                            icon = Icons.Filled.PlayArrow,
                            onClick = { onPlayLinkedVideo(short) },
                        )
                    }

                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.tv_shorts_remote_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                    )
                }
            }
        }
    }
}

@Composable
private fun TvShortAction(
    icon: ImageVector,
    label: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    focusRequester: FocusRequester? = null,
) {
    val focusState = rememberTvFocusState()
    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        TvIconButton(
            icon = icon,
            contentDescription = description,
            onClick = onClick,
            active = active,
            focusRequester = focusRequester,
            modifier = Modifier.tvFocusScale(focusState),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
