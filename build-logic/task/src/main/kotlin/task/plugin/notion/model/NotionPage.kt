package task.plugin.notion.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class NotionPage(
    val resourceId: String,
    val translations: Map<Language, String>
) {
    @Serializable
    private enum class Type {
        @SerialName("title")
        TITLE,

        @SerialName("rich_text")
        RICH_TEXT,
    }

    companion object {
        fun fromJsonElement(element: JsonElement): NotionPage? {
            val properties = element.jsonObject["properties"]?.jsonObject ?: return null

            val resourceId = properties.extractResourceId()
            if (resourceId.isBlank()) return null

            val translations = Language.values().associateWith { language ->
                properties.extractTranslation(language)
            }

            return NotionPage(resourceId, translations)
        }

        private fun JsonObject.extractResourceId(): String {
            return extractProperty("Resource ID")
                .lowercase()
                .replace(Regex("[^a-z0-9_]"), "_")
        }

        private fun JsonObject.extractTranslation(language: Language): String {
            return extractProperty(language.notionColumn)
                .escape()
                .toXliff()
        }

        private fun JsonObject.extractProperty(key: String): String {
            val property = this[key]?.jsonObject ?: return ""
            val typeString = property["type"]?.jsonPrimitive?.content ?: return ""
            val type = Json.decodeFromString<Type>("\"$typeString\"")

            return when (type) {
                Type.TITLE -> property["title"]?.jsonArray?.extractPlainText() ?: ""
                Type.RICH_TEXT -> property["rich_text"]?.jsonArray?.extractPlainText() ?: ""
            }
        }

        private fun JsonArray.extractPlainText(): String =
            joinToString("") { it.jsonObject["plain_text"]?.jsonPrimitive?.content ?: "" }

        private fun String.escape(): String = replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "\\'")
            .replace("\n", "\\n")
            .replace("\r", "")
            .replace("\t", " ")

        private fun String.toXliff(): String {
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
