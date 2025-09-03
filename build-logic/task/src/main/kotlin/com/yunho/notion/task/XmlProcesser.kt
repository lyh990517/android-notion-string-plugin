package com.yunho.notion.task

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

object XmlProcesser {
    private const val RESOURCE_NAME = "strings.xml"

    // XML structure constants
    private const val XML_DECLARATION = """<?xml version="1.0" encoding="utf-8"?>"""
    private const val XML_RESOURCES_OPEN = """<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">"""
    private const val XML_RESOURCES_CLOSE = "</resources>"
    private const val XML_STRING_TEMPLATE = """    <string name="%s">%s</string>"""

    // Property key constants
    private const val RESOURCE_ID_KEY = "Resource ID"

    // JSON property keys
    private const val TYPE_KEY = "type"
    private const val TITLE_KEY = "title"
    private const val RICH_TEXT_KEY = "rich_text"
    private const val FORMULA_KEY = "formula"
    private const val STRING_KEY = "string"
    private const val SELECT_KEY = "select"
    private const val MULTI_SELECT_KEY = "multi_select"
    private const val NAME_KEY = "name"
    private const val PLAIN_TEXT_KEY = "plain_text"
    private const val PROPERTIES_KEY = "properties"

    // XML escape characters
    private const val AMPERSAND = "&"
    private const val AMPERSAND_ESCAPED = "&amp;"
    private const val LESS_THAN = "<"
    private const val LESS_THAN_ESCAPED = "&lt;"
    private const val GREATER_THAN = ">"
    private const val GREATER_THAN_ESCAPED = "&gt;"
    private const val QUOTE = "\""
    private const val QUOTE_ESCAPED = "&quot;"
    private const val APOSTROPHE = "'"
    private const val APOSTROPHE_ESCAPED = "\\'"

    // Placeholder processing constants
    private const val PLACEHOLDER_REGEX = """\{([^}]+)\}"""
    private const val PLACEHOLDER_FORMAT = "%d\$s"
    private const val XLIFF_TAG_TEMPLATE = """<xliff:g id="%s" example="%s">%s</xliff:g>"""
    private const val INVALID_CHARS_REGEX = "[^a-z0-9_]"
    private const val REPLACEMENT_CHAR = "_"

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
                writer.appendLine(XML_DECLARATION)
                writer.appendLine(XML_RESOURCES_OPEN)

                results
                    .map { it.processString(language = language) }
                    .forEach { xmlData ->
                        writer.appendLine(XML_STRING_TEMPLATE.format(xmlData.resourceId, xmlData.stringValue))
                    }

                writer.appendLine(XML_RESOURCES_CLOSE)
            }
    }

    private fun JsonElement.processString(language: Language): XmlData {
        val properties = this.jsonObject[PROPERTIES_KEY]!!.jsonObject
        val resourceId = properties.extractRichText(key = RESOURCE_ID_KEY)
            .lowercase()
            .replace(Regex(INVALID_CHARS_REGEX), REPLACEMENT_CHAR)
        val stringValue = properties.extractRichText(key = language.notionColumn)
        val processedString = processPlaceholders(raw = stringValue.escapeXml())

        return XmlData(
            resourceId = resourceId,
            stringValue = processedString
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
        val propertyType = PropertyType.fromString(property[TYPE_KEY]!!.jsonPrimitive.content)

        return when (propertyType) {
            PropertyType.TITLE -> property[TITLE_KEY]!!.jsonArray
                .joinToString("") { it.jsonObject[PLAIN_TEXT_KEY]!!.jsonPrimitive.content }

            PropertyType.RICH_TEXT -> property[RICH_TEXT_KEY]!!.jsonArray
                .joinToString("") { it.jsonObject[PLAIN_TEXT_KEY]!!.jsonPrimitive.content }

            PropertyType.FORMULA -> property[FORMULA_KEY]!!.jsonObject[STRING_KEY]?.jsonPrimitive?.content.orEmpty()

            PropertyType.SELECT -> property[SELECT_KEY]?.jsonObject?.get(NAME_KEY)?.jsonPrimitive?.content.orEmpty()

            PropertyType.MULTI_SELECT -> property[MULTI_SELECT_KEY]!!.jsonArray
                .joinToString(", ") { it.jsonObject[NAME_KEY]!!.jsonPrimitive.content }

            null -> ""
        }
    }
}
