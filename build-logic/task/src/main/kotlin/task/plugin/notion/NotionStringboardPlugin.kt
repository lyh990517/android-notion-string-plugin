package task.plugin.notion

import org.gradle.api.Plugin
import org.gradle.api.Project
import task.plugin.notion.NotionConfig
import kotlin.jvm.java

class NotionStringboardPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("stringboard", NotionConfig::class.java)

        project.tasks.register("fetchStringboard", StringboardTask::class.java) {
            group = "notion"

            configure(extension)
        }
    }
}
