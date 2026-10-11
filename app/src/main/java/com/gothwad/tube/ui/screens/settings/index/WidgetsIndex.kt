package com.gothwad.tube.ui.screens.settings.index

import com.gothwad.tube.R
import com.gothwad.tube.ui.components.settings.SettingEntry
import com.gothwad.tube.ui.components.settings.SettingsDestination
import com.gothwad.tube.widget.core.FlowWidgetEntry
import com.gothwad.tube.widget.core.FlowWidgets

internal object WidgetsIndex {
    fun entryFor(widget: FlowWidgetEntry) =
        SettingEntry(
            key = "widgets.${widget.id}",
            title = widget.label,
            summary = widget.description,
            section = R.string.settings_widgets_add_header,
            keywords = R.string.settings_keywords_widgets,
            destination = SettingsDestination.WIDGETS,
        )

    val all = FlowWidgets.catalog.map(::entryFor)
}
