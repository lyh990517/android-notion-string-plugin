package com.yunho.notion.task

import com.google.gson.JsonObject
import org.gradle.internal.declarativedsl.analysis.DefaultDataClass.Empty.properties
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object NotionUtil {
    private val client = HttpClient.newHttpClient()

    fun queryNotionApi(
        notionApiKey: String,
        databaseId: String,
        queryBody: String
    ): NotionDatabaseResponse {
        val request = createRequest(
            databaseId = databaseId,
            notionApiKey = notionApiKey,
            queryBody = queryBody
        )
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            error("❌ Notion API error: HTTP ${response.statusCode()} → ${response.body()}")
        }

        return NotionDatabaseResponse.create(response)
    }

    fun JsonObject.extractRichText(key: String): String {
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

    private fun createRequest(
        databaseId: String,
        notionApiKey: String,
        queryBody: String
    ): HttpRequest? = HttpRequest.newBuilder()
        .uri(URI.create("https://api.notion.com/v1/databases/$databaseId/query"))
        .header("Authorization", notionApiKey)
        .header("Notion-Version", "2022-06-28")
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(queryBody))
        .build()
}
