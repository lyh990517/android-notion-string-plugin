package com.yunho.notion.task

import com.yunho.notion.task.NotionResponse.Companion.parseToNotionResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object NotionService {
    private val client = HttpClient.newHttpClient()

    fun queryNotionApi(
        notionApiKey: String,
        databaseId: String,
        queryBody: String
    ): NotionResponse {
        val request = createRequest(
            databaseId = databaseId,
            notionApiKey = notionApiKey,
            queryBody = queryBody
        )

        val response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        )

        return response.parseToNotionResponse()
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
