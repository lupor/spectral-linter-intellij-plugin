package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.ide.HelpTooltip
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import javax.swing.JTextArea
import javax.swing.JTextField

class SettingsComponent {

    val rulesetInput = JTextField()
    val includedFilesInput = JTextArea()
    lateinit var useNodePackageWin: Cell<JBCheckBox>
    lateinit var useFileOverrides: Cell<JBCheckBox>

    val mainPanel = panel {
        row("Ruleset:") {
            cell(rulesetInput)
        }
        row("Included path patterns:") {
            cell(includedFilesInput)
        }
        row {
            useFileOverrides = checkBox("Use file level overrides")
                .applyToComponent {
                    HelpTooltip()
                        .setDescription(
                            """
                            Preserves file names and allows to use <a href="https://docs.stoplight.io/docs/spectral/293426e270fac-overrides">file name
                            based overrides</a>, but comes at a cost. You have to save
                            the file for it to be in sync again with the linting output.
                            """.trimIndent()
                        )
                        .installOn(this)
                }
        }
        row {
            useNodePackageWin = checkBox("Use node package on Windows")
                .applyToComponent {
                    HelpTooltip()
                        .setDescription(
                            """
                            Adds workaround for Windows that is required
                            if Spectral was installed via npm. Has a slight
                            performance overhead. It is recommended to <a href="https://github.com/stoplightio/spectral/releases">use
                            the binary directly</a> on Windows machines.
                            """.trimIndent()
                        )
                        .installOn(this)
                }
        }
    }
}
