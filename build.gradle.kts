import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType


val kotlinxSerializationJsonVersion = "1.9.0"
val kotlinReflectVersion = "2.2.0"
val springCoreVersion = "6.2.8"
val junitJupiterVersion = "5.13.2"
val junit4Version = "4.13.2"
val commonsIoVersion = "2.19.0"
val intelliJJsonVersion = "252.23591.19"

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

val platformVersion = properties("platformVersion").get()

plugins {
    id("java")
    kotlin("jvm") version "2.2.0"
    id("org.jetbrains.intellij.platform") version "2.6.0"
    id("org.jetbrains.changelog") version "2.2.1"
    id("org.jetbrains.qodana") version "2025.1.1"
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
    kotlin("plugin.serialization") version "2.2.0"
}

// Set the JVM language level used to build the project. Use Java 11 for 2020.3+, and Java 17 for 2022.2+.
kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

// Configure project's dependencies
repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
        localPlatformArtifacts()
    }
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }
    test { useJUnitPlatform() }
}

val runIdeForUiTests by intellijPlatformTesting.runIde.registering {
    version = "2024.2"
    task {
        jvmArgumentProviders += CommandLineArgumentProvider {
            listOf(
                "-Drobot-server.port=8082",
                "-Dide.mac.message.dialogs.as.sheets=false",
                "-Djb.privacy.policy.text=<!--999.999-->",
                "-Djb.consents.confirmation.enabled=false",
            )
        }
    }

    plugins {
        robotServerPlugin()
    }
}


dependencies {
    intellijPlatform {
        intellijIdeaCommunity(platformVersion)
        val plugins = properties("platformBundledPlugins").get()
            .split(',')
            .map(String::trim)
            .filter(String::isNotEmpty)

        for (pluginId in plugins) {
            bundledPlugin(pluginId)
        }

        plugin("com.intellij.modules.json:$intelliJJsonVersion")

        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }

    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")

    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersion")
    implementation("org.springframework:spring-core:$springCoreVersion")
    implementation("commons-io:commons-io:$commonsIoVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Needed as workaround for https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-faq.html#junit5-test-framework-refers-to-junit4
    testRuntimeOnly("junit:junit:$junit4Version")
}

// Configure Gradle IntelliJ Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellijPlatform {
    pluginConfiguration {
        id.set(properties("pluginId"))
        name.set(properties("pluginName").get())
        version.set(properties("pluginVersion").get())

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }

        ideaVersion {
            sinceBuild = properties("pluginSinceBuild")
            untilBuild = properties("pluginUntilBuild")
        }
    }

    publishing {
        val publishToken = environment("PUBLISH_TOKEN")
        token.set(publishToken.getOrNull())
    }

    signing {
        val privateKeyPassword = environment("PRIVATE_KEY_PASSWORD")

        certificateChainFile.set(file("certificate/private.pem"))
        privateKeyFile.set(file("certificate/private.pem"))
        password.set(privateKeyPassword.getOrNull())
    }

    pluginVerification {
        ides {
            ide(IntelliJPlatformType.IntellijIdeaCommunity, platformVersion)
            recommended()
        }
    }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version.set(properties("pluginVersion").get())
    path.set(file("CHANGELOG.md").canonicalPath)
    itemPrefix.set("-")
    keepUnreleasedSection.set(true)
    unreleasedTerm.set("[Unreleased]")
    groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
    lineSeparator.set("\n")
    combinePreReleases.set(true)
}
