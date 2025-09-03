package com.yunho.notion.task

import com.yunho.notion.task.Key.AMPERSAND
import com.yunho.notion.task.Key.AMPERSAND_ESCAPED
import com.yunho.notion.task.Key.APOSTROPHE
import com.yunho.notion.task.Key.APOSTROPHE_ESCAPED
import com.yunho.notion.task.Key.FORMULA
import com.yunho.notion.task.Key.GREATER_THAN
import com.yunho.notion.task.Key.GREATER_THAN_ESCAPED
import com.yunho.notion.task.Key.INVALID_CHARS_REGEX
import com.yunho.notion.task.Key.LESS_THAN
import com.yunho.notion.task.Key.LESS_THAN_ESCAPED
import com.yunho.notion.task.Key.MULTI_SELECT
import com.yunho.notion.task.Key.NAME
import com.yunho.notion.task.Key.PLACEHOLDER_FORMAT
import com.yunho.notion.task.Key.PLACEHOLDER_REGEX
import com.yunho.notion.task.Key.PLAIN_TEXT
import com.yunho.notion.task.Key.PROPERTIES
import com.yunho.notion.task.Key.QUOTE
import com.yunho.notion.task.Key.QUOTE_ESCAPED
import com.yunho.notion.task.Key.REPLACEMENT_CHAR
import com.yunho.notion.task.Key.RICH_TEXT
import com.yunho.notion.task.Key.SELECT
import com.yunho.notion.task.Key.STRING
import com.yunho.notion.task.Key.TITLE
import com.yunho.notion.task.Key.TYPE
import com.yunho.notion.task.Key.XLIFF_TAG_TEMPLATE
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

object JsonParser {
    private enum class PropertyType(val typeName: String) {
        TITLE("title"),
        RICH_TEXT("rich_text"),
        FORMULA("formula"),
        SELECT("select"),
        MULTI_SELECT("multi_select");

        companion object {
            fun fromString(typeName: String): PropertyType? {
                return values().find { it.typeName == typeName }
            }
        }
    }

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
                            element.jsonObject[PROPERTIES]?.jsonObject
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

    private fun String.escapeXml() = replace(AMPERSAND, AMPERSAND_ESCAPED)
        .replace(LESS_THAN, LESS_THAN_ESCAPED)
        .replace(GREATER_THAN, GREATER_THAN_ESCAPED)
        .replace(QUOTE, QUOTE_ESCAPED)
        .replace(APOSTROPHE, APOSTROPHE_ESCAPED)

    private fun String.processPlaceholders(): String {
        var index = 1

        return Regex(PLACEHOLDER_REGEX).replace(this) { match ->
            val name = match.groupValues[1]
            val idName = name.lowercase().replace(Regex(INVALID_CHARS_REGEX), REPLACEMENT_CHAR)
            val placeholder = PLACEHOLDER_FORMAT.format(index)
            val tag = XLIFF_TAG_TEMPLATE.format(idName, name, placeholder)

            index++
            tag
        }
    }

    private fun JsonObject.extractId() = extractRichText(key = "Resource ID")
        .lowercase()
        .replace(Regex(INVALID_CHARS_REGEX), REPLACEMENT_CHAR)

    private fun JsonObject.extractString(language: Language) = extractRichText(key = language.notionColumn)
        .escapeXml()
        .processPlaceholders()

    private fun JsonObject.extractRichText(key: String): String {
        val property = this[key]?.jsonObject ?: return ""
        val propertyType = PropertyType.fromString(property[TYPE]?.jsonPrimitive?.content ?: return "")

        return when (propertyType) {
            PropertyType.TITLE -> property[TITLE]?.jsonArray
                ?.joinToString("") { it.jsonObject[PLAIN_TEXT]?.jsonPrimitive?.content ?: "" } ?: ""

            PropertyType.RICH_TEXT -> property[RICH_TEXT]?.jsonArray
                ?.joinToString("") { it.jsonObject[PLAIN_TEXT]?.jsonPrimitive?.content ?: "" } ?: ""

            PropertyType.FORMULA -> property[FORMULA]?.jsonObject?.get(STRING)?.jsonPrimitive?.content.orEmpty()

            PropertyType.SELECT -> property[SELECT]?.jsonObject?.get(NAME)?.jsonPrimitive?.content.orEmpty()

            PropertyType.MULTI_SELECT -> property[MULTI_SELECT]?.jsonArray
                ?.joinToString(", ") { it.jsonObject[NAME]?.jsonPrimitive?.content ?: "" } ?: ""

            null -> ""
        }
    }
}
