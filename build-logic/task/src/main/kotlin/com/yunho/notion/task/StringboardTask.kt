package com.yunho.notion.task

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.yunho.notion.task.NotionUtil.extractRichText
import com.yunho.notion.task.NotionUtil.queryNotionApi
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
            writeStringsXml(
                language = lang,
                dir = dir,
                results = JsonArray().apply { allResults.forEach { add(it) } }
            )
            println("✅ Generated ${lang.name.uppercase()} → ${dir.relativeTo(project.projectDir)}")
        }
    }

    fun String.escapeXml() = replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "\\'")

    fun writeStringsXml(
        language: Language,
        dir: File,
        results: JsonArray
    ) {
        dir.mkdirs()
        File(
            dir,
            "strings.xml"
        ).bufferedWriter().use { w ->
            w.appendLine("""<?xml version="1.0" encoding="utf-8"?>""")
            w.appendLine("""<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">""")

            for (element in results) {
                val properties = element.asJsonObject.getAsJsonObject("properties")
                val resourceId = properties.extractRichText(key = "Resource ID").lowercase().replace(Regex("[^a-z0-9_]"), "_")
                val stringValue = properties.extractRichText(key = language.notionColumn)
                val processedString = processPlaceholders(raw = stringValue.escapeXml())

                w.appendLine("""    <string name="$resourceId">$processedString</string>""")
            }

            w.appendLine("</resources>")
        }
    }

    fun processPlaceholders(raw: String): String {
        var index = 1

        return Regex("""\{([^}]+)\}""").replace(raw) { match ->
            val name = match.groupValues[1]
            val idName = name.lowercase().replace(Regex("[^a-z0-9_]"), "_")
            val placeholder = "%${index}${'$'}s"
            val tag = """<xliff:g id="$idName" example="$name">$placeholder</xliff:g>"""
            index++
            tag
        }
    }

    enum class Language(
        val notionColumn: String,
        val resDir: String
    ) {
        KOR(
            notionColumn = "String: KOR",
            resDir = "values-ko"
        ),
        JPN(
            notionColumn = "String: JPN",
            resDir = "values-ja"
        ),
        ENG(
            notionColumn = "String: BASE",
            resDir = "values"
        );

        companion object {
            fun createDir(baseDir: File): Map<Language, File> {
                return values().associateWith { language ->
                    File(baseDir, language.resDir)
                }
            }
        }
    }
}
