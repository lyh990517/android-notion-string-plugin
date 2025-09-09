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
        notionConfig.validate()

        val service = NotionService(notionConfig, logger)
        val stringResource = StringResource(notionConfig, logger)
        val notionPages = service.requestPages()

        stringResource.create(notionPages)
    }
}
