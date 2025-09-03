package com.yunho.notion.task

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

object JsonParser {
    fun JsonArray.createStringsXml(
        path: String,
    ) {
        Language.values().forEach { language ->
            val directory = File(path, language.resDir)
            val resourceName = "strings.xml"

            directory.mkdirs()

            File(directory, resourceName)
                .bufferedWriter()
                .use { writer ->
                    writer.appendLine("""<?xml version="1.0" encoding="utf-8"?>""")
                    writer.appendLine("""<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">""")

                    this
                        .mapNotNull { element ->
                            element.jsonObject["properties"]?.jsonObject
                        }.map { properties ->
                            val resourceId = properties.extractId()
                            val content = properties.extractString(language)

                            resourceId to content
                        }.forEach { (resourceId, content) ->
                            writer.appendLine("""    <string name="$resourceId">$content</string>""")
                        }

                    writer.appendLine("</resources>")
                }

            println("✅ Generated ${language.name.uppercase()} → ${directory.path}")
        }
    }

    private fun String.escapeXml() = replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "\\'")

    private fun String.processPlaceholders(): String {
        var index = 1

        return Regex("""\{([^}]+)\}""").replace(this) { match ->
            val name = match.groupValues[1]
            val idName = name.lowercase().replace(Regex("[^a-z0-9_]"), "_")
            val placeholder = "%d\$s".format(index)
            val tag = """<xliff:g id="%s" example="%s">%s</xliff:g>""".format(idName, name, placeholder)

            index++
            tag
        }
    }

    private fun JsonObject.extractId() = extractRichText(key = "Resource ID")
        .lowercase()
        .replace(Regex("[^a-z0-9_]"), "_")

    private fun JsonObject.extractString(language: Language) = extractRichText(key = language.notionColumn)
        .escapeXml()
        .processPlaceholders()

    private fun JsonObject.extractRichText(key: String): String {
        val property = this[key]?.jsonObject.orEmpty()
        val propertyType = property["type"]?.jsonPrimitive?.content

        return when (propertyType) {
            "title" -> property["title"]?.jsonArray?.extractPlainText()
            "rich_text" -> property["rich_text"]?.jsonArray?.extractPlainText()
            "formula" -> property["formula"]?.jsonObject?.get("string")?.jsonPrimitive?.content
            "select" -> property["select"]?.jsonObject?.getName()
            "multi_select" -> property["multi_select"]?.jsonArray?.extractNames()
            else -> null
        }.orEmpty()
    }

    private fun JsonArray.extractPlainText(): String =
        joinToString("") { it.jsonObject.getPlainText() }

    private fun JsonObject.getPlainText(): String =
        this["plain_text"]?.jsonPrimitive?.content.orEmpty()

    private fun JsonObject.getName(): String =
        this["name"]?.jsonPrimitive?.content.orEmpty()

    private fun JsonArray.extractNames(): String =
        joinToString(", ") { it.jsonObject.getName() }
}
