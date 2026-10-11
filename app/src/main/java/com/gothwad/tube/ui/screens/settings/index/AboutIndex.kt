package com.gothwad.tube.ui.screens.settings.index

import com.gothwad.tube.R
import com.gothwad.tube.ui.components.settings.SettingEntry
import com.gothwad.tube.ui.components.settings.SettingsDestination

internal object AboutIndex {
    private val page = SettingsDestination.ABOUT

    private fun entry(
        key: String,
        title: Int,
        section: Int,
        summary: Int? = null,
    ) = SettingEntry(
        key = "about.$key",
        title = title,
        summary = summary,
        section = section,
        keywords = R.string.settings_keywords_about,
        destination = page,
    )

    val changelog = entry("changelog", R.string.about_changelog, R.string.section_app, R.string.whats_new_in_flow)
    val website = entry("website", R.string.about_website, R.string.section_contact, R.string.about_website_address)
    val github = entry("github", R.string.github_label, R.string.section_contact, R.string.github_subtitle)
    val reddit = entry("reddit", R.string.about_reddit, R.string.section_contact, R.string.about_reddit_subtitle)
    val creator = entry("creator", R.string.about_creator, R.string.section_contact, R.string.about_creator_name)
    val flowProject = entry("flow_project", R.string.special_thanks_flow_title, R.string.section_special_thanks, R.string.special_thanks_flow_subtitle)
    val flowContributors = entry("flow_contributors", R.string.special_thanks_contributors_title, R.string.section_special_thanks, R.string.special_thanks_contributors_subtitle)
    val license = entry("license", R.string.about_license, R.string.section_legal, R.string.about_license_name)
    val newPipe = entry("newpipe", R.string.newpipe_extractor_title, R.string.section_legal, R.string.newpipe_extractor_subtitle)
    val deviceInfo = entry("device_info", R.string.about_device_info, R.string.section_device)

    val all = listOf(changelog, website, github, reddit, creator, flowProject, flowContributors, license, newPipe, deviceInfo)
}
