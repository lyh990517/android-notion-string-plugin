package com.yunho.notion.task

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object NotionUtil {
    fun queryNotionApi(
        notionApiKey: String,
        databaseId: String,
        queryBody: String
    ): Triple<JsonArray, String?, Boolean> {
        val client = HttpClient.newHttpClient()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.notion.com/v1/databases/$databaseId/query"))
            .header("Authorization", notionApiKey)
            .header("Notion-Version", "2022-06-28")
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(queryBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            error("❌ Notion API error: HTTP ${response.statusCode()} → ${response.body()}")
        }

        val json = JsonParser.parseString(response.body()).asJsonObject

        val results = json.getAsJsonArray("results")
        val nextCursor = json.get("next_cursor")?.takeIf { !it.isJsonNull }?.asString
        val hasMore = json.get("has_more")?.asBoolean ?: false

        return Triple(results, nextCursor, hasMore)
    }

    fun extractRichText(props: JsonObject, key: String): String {
        val prop = props[key]?.asJsonObject ?: return ""

        return when (prop["type"].asString) {
            "title" -> prop.getAsJsonArray("title")
                .joinToString("") { it.asJsonObject["plain_text"].asString }

            "rich_text" -> prop.getAsJsonArray("rich_text")
                .joinToString("") { it.asJsonObject["plain_text"].asString }

            "formula" -> prop
                .getAsJsonObject("formula")
                .get("string")
                ?.asString
                .orEmpty()

            "select" -> prop
                .getAsJsonObject("select")
                ?.get("name")
                ?.asString
                .orEmpty()

            "multi_select" -> prop.getAsJsonArray("multi_select")
                .joinToString(", ") { it.asJsonObject["name"].asString }

            else -> ""
        }
    }
}
