package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import com.schwarzit.spectralIntellijPlugin.getLogger
import java.time.Duration

@State(
    name = "com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState",
    storages = [Storage("spectral.xml")]
)
@Service(Service.Level.PROJECT)
class ProjectSettingsState : PersistentStateComponent<ProjectSettingsState> {
    companion object {
        private val logger = getLogger()
        private val timeout = Duration.ofSeconds(30)

        @JvmStatic
        fun getInstance(project: Project): ProjectSettingsState = project.service()
    }


    var ruleset: String = "https://raw.githubusercontent.com/baloise-incubator/spectral-ruleset/refs/heads/main/zalando.yml"
    var includedFiles: String = """
        **/*openapi.json
        **/*openapi.yml
        **/*openapi.yaml
    """.trimIndent()
    var useFileOverrides = false
    var useNodePackageWin = false

    override fun getState(): ProjectSettingsState {
        return this
    }

    override fun loadState(state: ProjectSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

}
