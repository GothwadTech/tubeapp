package com.gothwad.tube.ui.components.shared.card

import androidx.compose.runtime.Composable
import com.gothwad.tube.ui.components.shared.CollaboratorsBottomSheet
import com.gothwad.tube.ui.components.shared.quickactions.VideoQuickActionsBottomSheet

@Composable
internal fun VideoCardSheets(
    state: VideoCardState,
    showChannel: Boolean,
) {
    val sheets = state.sheets
    if (sheets.showQuickActions) {
        VideoQuickActionsBottomSheet(
            video = state.video,
            title = state.title,
            thumbnailUrl = state.thumbnailUrl,
            showChannel = showChannel,
            onDismiss = { sheets.showQuickActions = false },
        )
    }

    if (sheets.showCollaborators) {
        CollaboratorsBottomSheet(
            collaborators = state.collaborators,
            onDismiss = { sheets.showCollaborators = false },
        )
    }
}
