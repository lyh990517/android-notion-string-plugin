package task.plugin.notion

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import task.plugin.notion.model.NotionPage
import task.plugin.notion.model.NotionResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class NotionService(
    private val notionConfig: NotionConfig
) {
    private val httpClient = HttpClient.newHttpClient()

    fun requestPages() = buildList {
        var currentCursor: String? = null

        do {
            val query = notionConfig
                .queryBuilder
                .cursor(currentCursor)
                .build()

            val request = query.toRequest()
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            val notionResponse = response.toNotionResponse()

            addAll(notionResponse.pages)
            currentCursor = notionResponse.nextCursor
        } while (notionResponse.hasMore)
    }

    private fun String.toRequest(): HttpRequest = with(notionConfig) {
        HttpRequest.newBuilder()
            .uri(URI.create("https://api.notion.com/v1/data_sources/${dataSourceId}/query"))
            .header("Authorization", notionApiKey)
            .header("Notion-Version", NOTION_VERSION)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(this@toRequest))
            .build()
    }

    private fun HttpResponse<String>.toNotionResponse(): NotionResponse {
        val json = Json.parseToJsonElement(body()).jsonObject

        val results = json[RESULTS]?.jsonArray ?: throw IllegalStateException("No results found")
        val nextCursor = json[NEXT_CURSOR]?.takeUnless { it.toString() == NULL }?.jsonPrimitive?.content
        val hasMore = json[HAS_MORE]?.jsonPrimitive?.content?.toBoolean() ?: false

        val pages = results.mapNotNull { NotionPage.fromJsonElement(it) }

        return NotionResponse(
            pages = pages,
            nextCursor = nextCursor,
            hasMore = hasMore
        )
    }

    companion object {
        private const val NOTION_VERSION = "2025-09-03"
        private const val RESULTS = "results"
        private const val NEXT_CURSOR = "next_cursor"
        private const val HAS_MORE = "has_more"
        private const val NULL = "null"
    }
}
