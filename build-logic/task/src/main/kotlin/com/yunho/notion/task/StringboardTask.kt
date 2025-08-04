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
            .addMultiSelect("Part", Query.CONTAINS, "Client")
        val allResults = mutableListOf<JsonElement>()
        var startCursor: String? = null
        var hasMore: Boolean

        do {
            val query = queryBuilder.build(startCursor = startCursor)
            val (results, nextCursor, hasNext) = queryNotionApi(
                "apiKey",
                "databaseId",
                query
            )
            allResults += results.toList()
            startCursor = nextCursor
            hasMore = hasNext
        } while (hasMore)

        val baseRes = File(targetDir)
        val dirs = mapOf(
            Language.KOR to File(baseRes, "values"),
            Language.JPN to File(baseRes, "values-ja"),
            Language.ENG to File(baseRes, "values-en")
        )

        dirs.forEach { (lang, dir) ->
            writeStringsXml(lang, dir, JsonArray().apply { allResults.forEach { add(it) } })
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
        File(dir, "strings.xml").bufferedWriter().use { w ->
            w.appendLine("""<?xml version="1.0" encoding="utf-8"?>""")
            w.appendLine("""<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">""")

            for (elem in results) {
                val props = elem.asJsonObject.getAsJsonObject("properties")
                val idText =
                    extractRichText(props, "Client ID").lowercase().replace(Regex("[^a-z0-9_]"), "_")
                val rawValue = when (language) {
                    Language.KOR -> extractRichText(props, "String: BASE")
                    Language.JPN -> extractRichText(props, "String: JPN")
                    Language.ENG -> extractRichText(props, "String: ENG")
                }
                val processed = processPlaceholders(rawValue.escapeXml())

                w.appendLine("""    <string name="$idText">$processed</string>""")
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

    enum class Language {
        KOR,
        JPN,
        ENG
    }
}
