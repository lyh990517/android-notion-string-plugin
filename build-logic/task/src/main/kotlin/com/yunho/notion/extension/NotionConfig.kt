package com.yunho.notion.extension

import com.yunho.notion.query.NotionQueryBuilder

open class NotionConfig {
    var notionApiKey: String = ""
    var dataSourceId: String = ""
    var outputDir: String = ""
    var queryBuilder: NotionQueryBuilder = NotionQueryBuilder()
}
