package com.yunho.notion.task

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.io.File

object XmlProcesser {
    private const val RESOURCE_NAME = "strings.xml"

    private data class XmlData(
        val resourceId: String,
        val stringValue: String
    )

    fun writeStringsXml(
        language: Language,
        dir: File,
        results: JsonArray
    ) {
        dir.mkdirs()
        File(
            dir,
            RESOURCE_NAME
        ).bufferedWriter()
            .use { writer ->
                writer.appendLine("""<?xml version="1.0" encoding="utf-8"?>""")
                writer.appendLine("""<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">""")

                results
                    .map { it.processString(language = language) }
                    .forEach { xmlData ->
                        writer.appendLine("""    <string name="${xmlData.resourceId}">${xmlData.stringValue}</string>""")
                    }

                writer.appendLine("</resources>")
            }
    }

    private fun JsonElement.processString(language: Language): XmlData {
        val properties = this.asJsonObject.getAsJsonObject("properties")
        val resourceId = properties.extractRichText(key = "Resource ID").lowercase().replace(Regex("[^a-z0-9_]"), "_")
        val stringValue = properties.extractRichText(key = language.notionColumn)
        val processedString = processPlaceholders(raw = stringValue.escapeXml())

        return XmlData(
            resourceId = resourceId,
            stringValue = processedString
        )
    }

    private fun String.escapeXml() = replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "\\'")

    private fun processPlaceholders(raw: String): String {
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

    private fun JsonObject.extractRichText(key: String): String {
        val property = this[key]?.asJsonObject ?: return ""

        return when (property["type"].asString) {
            "title" -> property.getAsJsonArray("title")
                .joinToString("") { it.asJsonObject["plain_text"].asString }

            "rich_text" -> property.getAsJsonArray("rich_text")
                .joinToString("") { it.asJsonObject["plain_text"].asString }

            "formula" -> property
                .getAsJsonObject("formula")
                .get("string")
                ?.asString
                .orEmpty()

            "select" -> property
                .getAsJsonObject("select")
                ?.get("name")
                ?.asString
                .orEmpty()

            "multi_select" -> property.getAsJsonArray("multi_select")
                .joinToString(", ") { it.asJsonObject["name"].asString }

            else -> ""
        }
    }
}
