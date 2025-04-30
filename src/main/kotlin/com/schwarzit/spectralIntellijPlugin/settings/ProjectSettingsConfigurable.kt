package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

class ProjectSettingsConfigurable(private val project: Project) : Configurable {

    private lateinit var settingsComponent: SettingsComponent

    override fun createComponent(): JComponent {
        val settings = ProjectSettingsState.getInstance(project)
        settingsComponent = SettingsComponent()

        settingsComponent.rulesetInput.text = settings.ruleset
        settingsComponent.includedFilesInput.text = settings.includedFiles
        settingsComponent.useFileOverrides = settings.useFileOverrides
        settingsComponent.useNodePackageWin = settings.useNodePackageWin
        return settingsComponent.mainPanel
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return settingsComponent.rulesetInput
    }

    override fun isModified(): Boolean {
        val settings = ProjectSettingsState.getInstance(project)
        var modified = settingsComponent.rulesetInput.text != settings.ruleset
        modified = modified || settingsComponent.includedFilesInput.text != settings.includedFiles
        modified = modified || settingsComponent.useFileOverrides != settings.useFileOverrides
        modified = modified || settingsComponent.useNodePackageWin != settings.useNodePackageWin
        return modified
    }

    override fun apply() {
        val settings = ProjectSettingsState.getInstance(project)
        settings.ruleset = settingsComponent.rulesetInput.text
        settings.includedFiles = settingsComponent.includedFilesInput.text
        settings.useFileOverrides = settingsComponent.useFileOverrides
        settings.useNodePackageWin = settingsComponent.useNodePackageWin
    }

    override fun getDisplayName(): String {
        return "Spectral Linter"
    }
}