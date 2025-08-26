package com.yunho.notion.task

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.yunho.notion.task.NotionService.queryNotionApi
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

internal abstract class StringboardTask : DefaultTask() {
    @TaskAction
    fun download() {
        val targetDir = "${project.rootDir}/app/src/main/res"
        val queryBuilder = NotionQueryBuilder()
        val allResults = mutableListOf<JsonElement>()
        var startCursor: String? = null
        var hasMore: Boolean

        do {
            val query = queryBuilder.build(startCursor = startCursor)
            val (results, nextCursor, hasNext) = queryNotionApi(
                notionApiKey = "",
                databaseId = "",
                queryBody = query
            )
            allResults += results.toList()
            startCursor = nextCursor
            hasMore = hasNext
        } while (hasMore)

        val dirs = Language.createDir(baseDir = File(targetDir))

        dirs.forEach { (lang, dir) ->
            XmlProcesser.writeStringsXml(
                language = lang,
                dir = dir,
                results = JsonArray().apply { allResults.forEach { add(it) } }
            )

            println("✅ Generated ${lang.name.uppercase()} → ${dir.relativeTo(project.projectDir)}")
        }
    }
}
