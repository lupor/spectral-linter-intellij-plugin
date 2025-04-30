package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.ide.HelpTooltip
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import javax.swing.JTextArea
import javax.swing.JTextField

class SettingsComponent {

    val rulesetInput = JTextField()
    val includedFilesInput = JTextArea()
    var useFileOverrides = false
    var useNodePackageWin = false

    val mainPanel = panel {
        row("Ruleset:") {
            cell(rulesetInput)
        }
        row("Included path patterns:") {
            cell(includedFilesInput)
        }
        row {
            checkBox("Use file level overrides")
                .bindSelected({ useFileOverrides }, { useFileOverrides = it })
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
            checkBox("Use node package on Windows")
                .bindSelected({ useNodePackageWin }, { useNodePackageWin = it })
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
