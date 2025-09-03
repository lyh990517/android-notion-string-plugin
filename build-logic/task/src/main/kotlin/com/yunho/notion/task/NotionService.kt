package com.yunho.notion.task

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Notion API와의 통신을 담당하는 서비스 클래스
 */
object NotionService {

    private val httpClient = HttpClient.newHttpClient()
    private const val NOTION_VERSION = "2022-06-28"

    fun queryDatabase(
        apiKey: String,
        databaseId: String,
        query: String
    ): NotionResponse {
        val request = buildRequest(apiKey, databaseId, query)
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        return parseResponse(response)
    }

    private fun buildRequest(
        apiKey: String,
        databaseId: String,
        query: String
    ): HttpRequest {
        return HttpRequest.newBuilder()
            .uri(URI.create("https://api.notion.com/v1/databases/$databaseId/query"))
            .header("Authorization", apiKey)
            .header("Notion-Version", NOTION_VERSION)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(query))
            .build()
    }

    private fun parseResponse(response: HttpResponse<String>): NotionResponse {
        val json = Json.parseToJsonElement(response.body()).jsonObject

        val results = json["results"]?.jsonArray ?: throw IllegalStateException("No results found")
        val nextCursor = json["next_cursor"]?.takeUnless { it.toString() == "null" }?.jsonPrimitive?.content
        val hasMore = json["has_more"]?.jsonPrimitive?.content?.toBoolean() ?: false

        val pages = results.mapNotNull { NotionPageData.fromJsonElement(it) }

        return NotionResponse(
            pages = pages,
            nextCursor = nextCursor,
            hasMore = hasMore
        )
    }
}
