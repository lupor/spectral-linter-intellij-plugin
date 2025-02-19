package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

class ProjectSettingsConfigurable(private val project: Project) : Configurable {

    private lateinit var settingsComponent: SettingsComponent

    override fun createComponent(): JComponent {
        val settings = project.getService(ProjectSettingsState::class.java)
        settingsComponent = SettingsComponent()

        settingsComponent.rulesetInput.text = settings.ruleset
        settingsComponent.includedFilesInput.text = settings.includedFiles
        settingsComponent.useFileOverrides.isSelected = settings.useFileOverrides
        settingsComponent.useNodePackageWin.isSelected = settings.useNodePackageWin
        return settingsComponent.mainPanel
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return settingsComponent.rulesetInput
    }

    override fun isModified(): Boolean {
        val settings = project.service<ProjectSettingsState>()
        var modified = settingsComponent.rulesetInput.text != settings.ruleset
        modified = modified || settingsComponent.includedFilesInput.text != settings.includedFiles
        modified = modified || settingsComponent.useFileOverrides.isSelected != settings.useFileOverrides
        modified = modified || settingsComponent.useNodePackageWin.isSelected != settings.useNodePackageWin
        return modified
    }

    override fun apply() {
        val settings = project.service<ProjectSettingsState>()
        settings.ruleset = settingsComponent.rulesetInput.text
        settings.includedFiles = settingsComponent.includedFilesInput.text
        settings.useFileOverrides = settingsComponent.useFileOverrides.isSelected
        settings.useNodePackageWin = settingsComponent.useNodePackageWin.isSelected
    }

    override fun getDisplayName(): String {
        return "Spectral Linter"
    }
}