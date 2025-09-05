package task.plugin.notion

import task.plugin.notion.query.NotionQueryBuilder

open class NotionConfig {
    var notionApiKey: String = ""
    var dataSourceId: String = ""
    var outputDir: String = ""
    var queryBuilder: NotionQueryBuilder = NotionQueryBuilder()
}
