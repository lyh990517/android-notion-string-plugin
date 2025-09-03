package com.yunho.notion.task

import com.yunho.notion.task.NotionService.queryNotionApi
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

internal abstract class StringboardTask : DefaultTask() {
    @TaskAction
    fun download() {
        val queryBuilder = NotionQueryBuilder()
        val allResults = mutableListOf<JsonElement>()
        var startCursor: String? = null
        var hasMore: Boolean

        do {
            val query = queryBuilder.build(startCursor = startCursor)
            val response = queryNotionApi(
                notionApiKey = "",
                databaseId = "",
                queryBody = query
            )

            allResults += response.results.toList()
            startCursor = response.nextCursor
            hasMore = response.hasMore
        } while (hasMore)

        JsonParser.createStringsXml(
            path = "${project.rootDir}/app/src/main/res",
            results = JsonArray(allResults)
        )
    }
}
