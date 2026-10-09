import com.diffplug.spotless.LineEnding

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.10")
    }
}

// Top-level build file
plugins {
    id("com.android.application") version "9.3.1" apply false
    id("com.android.library") version "9.3.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.20" apply false
    id("com.google.dagger.hilt.android") version "2.60.1" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.20" apply false
    id("com.google.devtools.ksp") version "2.3.11" apply false
    id("com.android.test") version "9.3.1" apply false
    id("androidx.baselineprofile") version "1.5.0-rc01" apply false
    id("com.diffplug.spotless") version "8.10.0"
    alias(libs.plugins.room) apply false
}

spotless {
    // Adopt formatting incrementally from the commit that imported the source tree into this
    // repository. Upstream ratcheted from 52c4928e5af05141080f46f6c1e41cbf9c457023, a commit in
    // A-EDev/Flow's history; that object does not exist here, so spotlessCheck could not resolve
    // its ratchet base and failed. Re-anchoring on our own import commit preserves the original
    // intent: existing files are grandfathered in, everything changed from here on is enforced.
    ratchetFrom("4908f99aab7c6282374dd4359e52ac0ee57f1866")
    lineEndings = LineEnding.UNIX

    val ktlintConfig =
        mapOf(
            "ij_kotlin_packages_to_use_import_on_demand" to "**",
        )

    kotlin {
        target(
            "app/src/**/*.kt",
            "benchmark/src/**/*.kt",
        )
        targetExclude(
            "**/build/**",
            "**/generated/**",
        )
        ktlint("1.8.0").editorConfigOverride(ktlintConfig)
    }

    kotlinGradle {
        target(
            "*.gradle.kts",
            "app/*.gradle.kts",
            "benchmark/*.gradle.kts",
        )
        targetExclude(
            "**/build/**",
        )
        ktlint("1.8.0").editorConfigOverride(ktlintConfig)
    }
}

tasks.register("ktlintCheck") {
    group = "verification"
    description = "Checks Kotlin formatting with ktlint."
    dependsOn("spotlessCheck")
}

tasks.register("ktlintFormat") {
    group = "formatting"
    description = "Formats changed Kotlin files with ktlint."
    dependsOn("spotlessApply")
}
