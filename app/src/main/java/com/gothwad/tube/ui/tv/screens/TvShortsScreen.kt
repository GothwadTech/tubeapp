package com.gothwad.tube.ui.tv.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.gothwad.tube.R
import com.gothwad.tube.data.local.PlayerPreferences
import com.gothwad.tube.data.model.ShortVideo
import com.gothwad.tube.data.model.Video
import com.gothwad.tube.data.shorts.queue.ShortsQueueSource
import com.gothwad.tube.player.shorts.ShortsPlayerPool
import com.gothwad.tube.ui.components.shorts.rememberShortsReelSettings
import com.gothwad.tube.ui.screens.shorts.ShortsPagerPlaybackEffects
import com.gothwad.tube.ui.screens.shorts.ShortsViewModel
import com.gothwad.tube.ui.screens.shorts.shortsTargetHeight
import com.gothwad.tube.ui.theme.PlayerScrim
import com.gothwad.tube.ui.theme.PlayerScrimContent
import com.gothwad.tube.ui.tv.components.TvButton
import com.gothwad.tube.ui.tv.components.TvLoadingState
import com.gothwad.tube.ui.tv.input.TvShortsAction
import com.gothwad.tube.ui.tv.input.TvShortsKeyMapper
import com.gothwad.tube.ui.tv.theme.LocalTvDimens
import com.gothwad.tube.ui.utils.rememberIsOnWifi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun TvShortsScreen(
    source: ShortsQueueSource,
    onBack: () -> Unit,
    onChannelClick: (String) -> Unit,
    onPlayVideo: (Video) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShortsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val playerPreferences = remember(context) { PlayerPreferences(context.applicationContext) }
    val settings = rememberShortsReelSettings(playerPreferences)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isWifi = rememberIsOnWifi()
    val qualityPair by remember(playerPreferences) {
        combine(playerPreferences.shortsQualityWifi, playerPreferences.shortsQualityCellular) { wifi, cellular -> wifi to cellular }
    }.collectAsStateWithLifecycle(initialValue = null)
    val targetHeight by remember(isWifi, qualityPair) {
        derivedStateOf { qualityPair?.let { (wifi, cellular) -> shortsTargetHeight(isWifi, wifi, cellular) } }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    var activePanel by remember(source) { mutableStateOf<TvShortsPanel?>(null) }

    LaunchedEffect(source) { viewModel.load(source) }

    DisposableEffect(viewModel) {
        val pool = ShortsPlayerPool.getInstance()
        val hostToken = pool.acquireHost()
        viewModel.onScreenVisible()
        onDispose {
            viewModel.onScreenHidden()
            pool.releaseIfHost(hostToken)
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
            viewModel.clearSnackbar()
        }
    }

    BackHandler(enabled = true) {
        if (activePanel != null) {
            activePanel = null
        } else {
            onBack()
        }
    }

    Box(
        modifier = modifier.fillMaxSize().background(PlayerScrim),
    ) {
        when {
            uiState.isLoading && uiState.shorts.isEmpty() -> {
                TvLoadingState(modifier = Modifier.fillMaxSize())
            }

            uiState.error != null && uiState.shorts.isEmpty() -> {
                TvShortsEmptyState(
                    title = stringResource(R.string.error_failed_to_load_shorts),
                    message = uiState.error,
                    primaryAction = stringResource(R.string.retry),
                    onPrimaryAction = { viewModel.retry(source) },
                    secondaryAction = stringResource(R.string.close),
                    onSecondaryAction = onBack,
                )
            }

            uiState.shorts.isEmpty() -> {
                TvShortsEmptyState(
                    title = stringResource(R.string.error_no_shorts_found),
                    message = null,
                    primaryAction = stringResource(R.string.retry),
                    onPrimaryAction = { viewModel.retry(source) },
                    secondaryAction = stringResource(R.string.close),
                    onSecondaryAction = onBack,
                )
            }

            else -> {
                TvShortsFeed(
                    shorts = uiState.shorts,
                    initialIndex = uiState.currentIndex,
                    isLoadingMore = uiState.isLoadingMore,
                    targetHeight = targetHeight,
                    playerPreferences = playerPreferences,
                    settings = settings,
                    viewModel = viewModel,
                    activePanel = activePanel,
                    onPanelChange = { activePanel = it },
                    onBack = onBack,
                    onChannelClick = onChannelClick,
                    onPlayVideo = onPlayVideo,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp),
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                shape = MaterialTheme.shapes.medium,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun TvShortsFeed(
    shorts: List<ShortVideo>,
    initialIndex: Int,
    isLoadingMore: Boolean,
    targetHeight: Int?,
    playerPreferences: PlayerPreferences,
    settings: com.gothwad.tube.ui.components.shorts.ShortsReelSettings,
    viewModel: ShortsViewModel,
    activePanel: TvShortsPanel?,
    onPanelChange: (TvShortsPanel?) -> Unit,
    onBack: () -> Unit,
    onChannelClick: (String) -> Unit,
    onPlayVideo: (Video) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(initialPage = initialIndex.coerceIn(0, shorts.lastIndex), pageCount = { shorts.size })
    val scope = rememberCoroutineScope()
    val videoFocusRequester = remember { FocusRequester() }
    val firstActionFocusRequester = remember { FocusRequester() }
    val currentShort = shorts.getOrNull(pagerState.currentPage)

    LaunchedEffect(pagerState.currentPage) {
        viewModel.updateCurrentIndex(pagerState.currentPage)
    }

    LaunchedEffect(pagerState.settledPage) {
        withFrameNanos { }
        runCatching { videoFocusRequester.requestFocus() }
    }

    ShortsPagerPlaybackEffects(
        pagerState = pagerState,
        shorts = shorts,
        targetHeight = targetHeight,
        playerPreferences = playerPreferences,
        viewModel = viewModel,
    )

    fun moveTo(delta: Int) {
        if (pagerState.isScrollInProgress) return
        val target = pagerState.currentPage + delta
        when {
            target in shorts.indices -> scope.launch { pagerState.animateScrollToPage(target) }
            delta > 0 && isLoadingMore -> Unit
            delta > 0 -> viewModel.loadMoreShorts()
        }
    }

    Box(
        modifier =
            modifier
                .background(PlayerScrim)
                .onPreviewKeyEvent { event ->
                    if (event.type != KeyEventType.KeyDown || activePanel != null) return@onPreviewKeyEvent false
                    val nativeEvent = event.nativeKeyEvent
                    val action = TvShortsKeyMapper.map(nativeEvent.keyCode) ?: return@onPreviewKeyEvent false
                    when (action) {
                        TvShortsAction.NEXT -> {
                            if (nativeEvent.repeatCount == 0) moveTo(1)
                            true
                        }

                        TvShortsAction.PREVIOUS -> {
                            if (nativeEvent.repeatCount == 0) moveTo(-1)
                            true
                        }

                        TvShortsAction.TOGGLE_PLAYBACK -> {
                            ShortsPlayerPool.getInstance().togglePlayPause()
                            true
                        }

                        TvShortsAction.PLAY -> {
                            ShortsPlayerPool.getInstance().play()
                            true
                        }

                        TvShortsAction.PAUSE -> {
                            ShortsPlayerPool.getInstance().pause()
                            true
                        }

                        TvShortsAction.SEEK_BACK -> {
                            val pool = ShortsPlayerPool.getInstance()
                            pool.seekTo((pool.playbackPosition() - SHORTS_REMOTE_SEEK_MS).coerceAtLeast(0L))
                            true
                        }

                        TvShortsAction.SEEK_FORWARD -> {
                            val pool = ShortsPlayerPool.getInstance()
                            val duration = pool.playbackDuration()
                            val target = pool.playbackPosition() + SHORTS_REMOTE_SEEK_MS
                            pool.seekTo(if (duration > 0L) target.coerceAtMost(duration) else target)
                            true
                        }
                    }
                },
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().focusGroup(),
            userScrollEnabled = activePanel == null,
            beyondViewportPageCount = 1,
            key = { shorts[it].id },
        ) { page ->
            val short = shorts[page]
            val isActive = page == pagerState.currentPage
            TvShortsReelPage(
                short = short,
                isActive = isActive,
                pageIndex = page,
                pageNumber = page + 1,
                totalPages = shorts.size,
                viewModel = viewModel,
                settings = settings,
                activePanel = activePanel,
                videoFocusRequester = videoFocusRequester.takeIf { isActive },
                firstActionFocusRequester = firstActionFocusRequester.takeIf { isActive },
                onBack = onBack,
                onPrevious = { moveTo(-1) },
                onNext = { moveTo(1) },
                onOpenComments = { onPanelChange(TvShortsPanel.COMMENTS) },
                onOpenDescription = {
                    viewModel.loadShortDescription(short.id)
                    onPanelChange(TvShortsPanel.DESCRIPTION)
                },
                onOpenMore = { onPanelChange(TvShortsPanel.MORE) },
                onOpenChannel = {
                    short.channelId.takeIf(String::isNotBlank)?.let { id ->
                        onChannelClick("https://www.youtube.com/channel/$id")
                    }
                },
                onPlayLinkedVideo = { item ->
                    item.linkedVideo?.let { linked ->
                        onPlayVideo(
                            Video(
                                id = linked.id,
                                title = linked.title.orEmpty(),
                                channelName = item.channelName,
                                channelId = item.channelId,
                                thumbnailUrl = item.thumbnailUrl,
                                duration = 0,
                                viewCount = 0L,
                                uploadDate = "",
                            ),
                        )
                    }
                },
            )
        }

        currentShort?.let { short ->
            TvShortsPanelHost(
                activePanel = activePanel,
                short = short,
                viewModel = viewModel,
                onClose = { onPanelChange(null) },
            )
        }

        if (isLoadingMore) {
            androidx.compose.material3.LinearProgressIndicator(
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun TvShortsEmptyState(
    title: String,
    message: String?,
    primaryAction: String,
    onPrimaryAction: () -> Unit,
    secondaryAction: String,
    onSecondaryAction: () -> Unit,
) {
    val dimens = LocalTvDimens.current
    Box(Modifier.fillMaxSize().background(PlayerScrim), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(horizontal = dimens.overscanHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = PlayerScrimContent,
            )
            message?.takeIf(String::isNotBlank)?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TvButton(text = primaryAction, onClick = onPrimaryAction)
                TvButton(text = secondaryAction, onClick = onSecondaryAction)
            }
        }
    }
}

private const val SHORTS_REMOTE_SEEK_MS = 5_000L
