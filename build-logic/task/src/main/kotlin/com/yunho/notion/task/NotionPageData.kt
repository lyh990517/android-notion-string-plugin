package com.yunho.notion.task

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class NotionPageData(
    val resourceId: String,
    val translations: Map<Language, String>
) {
    companion object {
        fun fromJsonElement(element: JsonElement): NotionPageData? {
            val properties = element.jsonObject["properties"]?.jsonObject ?: return null

            val resourceId = properties.extractResourceId()
            if (resourceId.isBlank()) return null

            val translations = Language.values().associateWith { language ->
                properties.extractTranslation(language)
            }

            return NotionPageData(resourceId, translations)
        }

        private fun JsonObject.extractResourceId(): String {
            return extractProperty("Resource ID")
                .lowercase()
                .replace(Regex("[^a-z0-9_]"), "_")
        }

        private fun JsonObject.extractTranslation(language: Language): String {
            return extractProperty(language.notionColumn)
                .escapeXml()
                .processPlaceholders()
        }

        private fun JsonObject.extractProperty(key: String): String {
            val property = this[key]?.jsonObject ?: return ""
            val type = property["type"]?.jsonPrimitive?.content ?: return ""

            return when (type) {
                "title" -> property["title"]?.jsonArray?.extractPlainText() ?: ""
                "rich_text" -> property["rich_text"]?.jsonArray?.extractPlainText() ?: ""
                "formula" -> property["formula"]?.jsonObject?.get("string")?.jsonPrimitive?.content ?: ""
                "select" -> property["select"]?.jsonObject?.get("name")?.jsonPrimitive?.content ?: ""
                "multi_select" -> property["multi_select"]?.jsonArray?.extractNames() ?: ""
                else -> ""
            }
        }

        private fun JsonArray.extractPlainText(): String =
            joinToString("") { it.jsonObject["plain_text"]?.jsonPrimitive?.content ?: "" }

        private fun JsonArray.extractNames(): String =
            joinToString(", ") { it.jsonObject["name"]?.jsonPrimitive?.content ?: "" }

        private fun String.escapeXml(): String =
            replace("&", "&amp;")
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
                """<xliff:g id="$idName" example="$name">$placeholder</xliff:g>""".also { index++ }
            }
        }
    }
}
