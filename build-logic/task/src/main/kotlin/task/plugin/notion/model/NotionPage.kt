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
import org.gradle.api.logging.Logger
import task.plugin.notion.NotionConfig

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

        @SerialName("formula")
        FORMULA
    }

    companion object {
        fun fromJsonElement(
            notionConfig: NotionConfig,
            element: JsonElement,
            logger: Logger? = null
        ): NotionPage? {
            val properties = element.jsonObject["properties"]?.jsonObject
            if (properties == null) {
                logger?.lifecycle("❌ No properties found in element")
                return null
            }

            val resourceId = properties.extractResourceId(notionConfig.idPropertyName, logger)
            if (resourceId.isBlank()) {
                logger?.lifecycle("❌ Resource ID is blank for property: '${notionConfig.idPropertyName}'")
                return null
            }

            val translations = notionConfig.languages.associateWith { language ->
                properties.extractTranslation(language, logger)
            }

            return NotionPage(resourceId, translations)
        }

        private fun JsonObject.extractResourceId(idPropertyName: String, logger: Logger? = null): String {
            val extracted = extractProperty(idPropertyName, logger)
            return extracted
                .lowercase()
                .replace(Regex("[^a-z0-9_]"), "_")
        }

        private fun JsonObject.extractTranslation(language: Language, logger: Logger? = null): String {
            return extractProperty(language.property, logger)
                .escape()
                .toXliff()
        }

        private fun JsonObject.extractProperty(key: String, logger: Logger? = null): String {
            val property = this[key]?.jsonObject
            if (property == null) {
                logger?.lifecycle("❌ Property '$key' not found")
                return ""
            }

            val typeString = property["type"]?.jsonPrimitive?.content
            if (typeString == null) {
                logger?.lifecycle("❌ Type not found for property '$key'")
                return ""
            }

            val type = try {
                Json.decodeFromString<Type>("\"$typeString\"")
            } catch (e: Exception) {
                logger?.lifecycle("❌ Unknown type '$typeString' for property '$key'")
                return ""
            }

            return when (type) {
                Type.TITLE -> property["title"]?.jsonArray?.extractPlainText() ?: ""
                Type.RICH_TEXT -> property["rich_text"]?.jsonArray?.extractPlainText() ?: ""
                Type.FORMULA -> property["formula"]?.jsonObject?.get("string")?.jsonPrimitive?.content ?: ""
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
