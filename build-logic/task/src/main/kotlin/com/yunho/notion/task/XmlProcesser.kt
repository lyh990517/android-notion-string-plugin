package com.yunho.notion.task

import com.google.gson.JsonArray
import com.yunho.notion.task.NotionService.extractRichText
import java.io.File

object XmlProcesser {
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
        ).bufferedWriter()
            .use { writer ->
                writer.appendLine("""<?xml version="1.0" encoding="utf-8"?>""")
                writer.appendLine("""<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">""")

                for (element in results) {
                    val properties = element.asJsonObject.getAsJsonObject("properties")
                    val resourceId = properties.extractRichText(key = "Resource ID").lowercase().replace(Regex("[^a-z0-9_]"), "_")
                    val stringValue = properties.extractRichText(key = language.notionColumn)
                    val processedString = processPlaceholders(raw = stringValue.escapeXml())

                    writer.appendLine("""    <string name="$resourceId">$processedString</string>""")
                }

                writer.appendLine("</resources>")
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
}
