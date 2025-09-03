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
import com.yunho.notion.task.Key.RESOURCE_ID
import com.yunho.notion.task.Key.RESOURCE_NAME
import com.yunho.notion.task.Key.RICH_TEXT
import com.yunho.notion.task.Key.SELECT
import com.yunho.notion.task.Key.STRING
import com.yunho.notion.task.Key.TITLE
import com.yunho.notion.task.Key.TYPE
import com.yunho.notion.task.Key.XLIFF_TAG_TEMPLATE
import com.yunho.notion.task.Key.XML_DECLARATION
import com.yunho.notion.task.Key.XML_RESOURCES_CLOSE
import com.yunho.notion.task.Key.XML_RESOURCES_OPEN
import com.yunho.notion.task.Key.XML_STRING_TEMPLATE
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
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

    private data class XmlData(
        val resourceId: String,
        val content: String
    )

    fun createStringsXml(
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
                writer.appendLine(XML_DECLARATION)
                writer.appendLine(XML_RESOURCES_OPEN)

                results
                    .map { it.processString(language = language) }
                    .forEach { xmlData ->
                        writer.appendLine(XML_STRING_TEMPLATE.format(xmlData.resourceId, xmlData.content))
                    }

                writer.appendLine(XML_RESOURCES_CLOSE)
            }
    }

    private fun JsonElement.processString(language: Language): XmlData {
        val properties = this.jsonObject[PROPERTIES]?.jsonObject ?: return XmlData(
            resourceId = "",
            content = ""
        )
        val resourceId = properties.extractRichText(key = RESOURCE_ID)
            .lowercase()
            .replace(Regex(INVALID_CHARS_REGEX), REPLACEMENT_CHAR)
        val stringValue = properties.extractRichText(key = language.notionColumn)
        val processedString = processPlaceholders(raw = stringValue.escapeXml())

        return XmlData(
            resourceId = resourceId,
            content = processedString
        )
    }

    private fun String.escapeXml() = replace(AMPERSAND, AMPERSAND_ESCAPED)
        .replace(LESS_THAN, LESS_THAN_ESCAPED)
        .replace(GREATER_THAN, GREATER_THAN_ESCAPED)
        .replace(QUOTE, QUOTE_ESCAPED)
        .replace(APOSTROPHE, APOSTROPHE_ESCAPED)

    private fun processPlaceholders(raw: String): String {
        var index = 1

        return Regex(PLACEHOLDER_REGEX).replace(raw) { match ->
            val name = match.groupValues[1]
            val idName = name.lowercase().replace(Regex(INVALID_CHARS_REGEX), REPLACEMENT_CHAR)
            val placeholder = PLACEHOLDER_FORMAT.format(index)
            val tag = XLIFF_TAG_TEMPLATE.format(idName, name, placeholder)

            index++
            tag
        }
    }

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
