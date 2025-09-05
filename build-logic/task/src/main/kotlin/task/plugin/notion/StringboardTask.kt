package task.plugin.notion

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

internal abstract class StringboardTask : DefaultTask() {
    private lateinit var notionConfig: NotionConfig

    fun configure(config: NotionConfig) {
        this.notionConfig = config
    }

    @TaskAction
    fun download() {
        val service = NotionService(notionConfig)
        val notionPages = service.requestPages()

        StringResource.create(
            pages = notionPages,
            outputDir = notionConfig.outputDir
        )
    }
}
