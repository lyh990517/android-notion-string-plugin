package com.yunho.notion.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

internal abstract class StringboardTask : DefaultTask() {
    private val notionApiKey = ""
    private val stringDatabaseId = ""

    @TaskAction
    fun download() {
        val notionData = fetchAllNotionData()
        val outputPath = "${project.rootDir}/$OUTPUT_PATH"

        StringResourceGenerator.generateAll(notionData, outputPath)
    }

    private fun fetchAllNotionData(): List<NotionPageData> {
        val queryBuilder = NotionQueryBuilder()
            .addSelect(
                property = "Status",
                query = Query.EQUALS,
                value = "Done"
            )
        val allPages = mutableListOf<NotionPageData>()
        var cursor: String? = null

        do {
            val query = queryBuilder.build(startCursor = cursor)
            val response = NotionService.queryDatabase(
                apiKey = notionApiKey,
                databaseId = stringDatabaseId,
                query = query
            )

            allPages += response.pages
            cursor = response.nextCursor
        } while (response.hasMore)

        return allPages
    }

    companion object {
        private const val OUTPUT_PATH = "app/src/main/res"
    }
}
