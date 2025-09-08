package task.plugin.notion

import org.gradle.api.GradleException
import task.plugin.notion.model.Language
import task.plugin.notion.query.NotionQueryBuilder

open class NotionConfig {
    var notionApiKey: String = ""
    var dataSourceId: String = ""
    var outputDir: String = ""
    var queryBuilder: NotionQueryBuilder = NotionQueryBuilder()
    var idPropertyName: String = ""
    var languages: List<Language> = listOf()

    fun validate() {
        if (notionApiKey.isBlank()) {
            throw GradleException("notionApiKey must be configured")
        }
        if (dataSourceId.isBlank()) {
            throw GradleException("dataSourceId must be configured")
        }
        if (outputDir.isBlank()) {
            throw GradleException("outputDir must be configured")
        }
        if (idPropertyName.isBlank()) {
            throw GradleException("idPropertyName must be configured")
        }
        if (languages.isEmpty()) {
            throw GradleException("languages must be configured with at least one language")
        }
    }
}
