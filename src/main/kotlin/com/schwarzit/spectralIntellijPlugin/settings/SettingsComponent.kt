package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.ide.HelpTooltip
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class SettingsComponent {

    val mainPanel: JPanel
    val rulesetInput = JBTextField()
    val includedFilesInput = JBTextArea()
    val useFileOverrides = JBCheckBox("Use file level overrides", false)
    val useNodePackageWin = JBCheckBox("Use node package on on Windows", false)

    init {
        val overrideTooltip = """
            Preserves file names and allows to use <a href="https://docs.stoplight.io/docs/spectral/293426e270fac-overrides">file name
            based overrides</a>, but comes at a cost. You have to save
            the file for it to be in sync again with the linting output.
        """.trimIndent()
        HelpTooltip()
            .setDescription(overrideTooltip)
            .installOn(useFileOverrides)

        val useNodePackageWinTooltip = """
            Adds workaround for Windows that is required
            if Spectral was installed via npm. Has a slight 
            performance overhead. It is recommended to <a href="https://github.com/stoplightio/spectral/releases">use 
            the binary directly</a> on Windows machines.
        """.trimIndent()
        HelpTooltip()
            .setDescription(useNodePackageWinTooltip)
            .installOn(useNodePackageWin)
        mainPanel =
            FormBuilder.createFormBuilder()
                .addComponent(JBLabel("Ruleset"))
                .addComponent(rulesetInput)
                .addLabeledComponent(JBLabel("Included path patterns"), includedFilesInput)
                .addComponent(useFileOverrides)
                .addComponent(useNodePackageWin)
                .addComponentFillVertically(JPanel(), 0)
                .panel
    }


}
