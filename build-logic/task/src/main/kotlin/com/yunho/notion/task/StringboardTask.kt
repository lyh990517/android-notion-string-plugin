package com.yunho.notion.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

internal abstract class StringboardTask : DefaultTask() {

    companion object {
        private const val OUTPUT_PATH = "app/src/main/res"
    }

    @TaskAction
    fun download() {
        val notionData = fetchAllNotionData()
        val outputPath = "${project.rootDir}/$OUTPUT_PATH"

        StringResourceGenerator.generateAll(notionData, outputPath)
    }

    private fun fetchAllNotionData(): List<NotionPageData> {
        val queryBuilder = NotionQueryBuilder()
        val allPages = mutableListOf<NotionPageData>()
        var cursor: String? = null

        do {
            val query = queryBuilder.build(startCursor = cursor)
            val response = NotionService.queryDatabase(
                apiKey = getNotionApiKey(),
                databaseId = getDatabaseId(),
                query = query
            )

            allPages += response.pages
            cursor = response.nextCursor
        } while (response.hasMore)

        return allPages
    }

    private fun getNotionApiKey(): String = ""  // TODO: Get from project properties
    private fun getDatabaseId(): String = ""   // TODO: Get from project properties
}
