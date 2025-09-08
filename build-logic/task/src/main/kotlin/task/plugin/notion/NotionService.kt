package task.plugin.notion

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.logging.Logger
import task.plugin.notion.model.NotionPage
import task.plugin.notion.model.NotionResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class NotionService(
    private val notionConfig: NotionConfig,
    private val logger: Logger
) {
    private val httpClient = HttpClient.newHttpClient()

    fun requestPages() = buildList {
        var currentCursor: String? = null
        var pageCount = 0

        logger.lifecycle("🔄 Starting to fetch pages from Notion...")

        do {
            val query = notionConfig
                .queryBuilder
                .cursor(currentCursor)
                .build()

            val request = query.toRequest()
            logger.lifecycle("📡 Requesting pages from Notion API...")
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            logger.lifecycle("📥 Received response code : ${response.statusCode()}")
            logger.lifecycle("📥 Received response body : ${response.body()}")
            val notionResponse = response.toNotionResponse()

            addAll(notionResponse.pages)
            pageCount += notionResponse.pages.size
            currentCursor = notionResponse.nextCursor

            logger.lifecycle("📄 Fetched ${notionResponse.pages.size} pages (Total: $pageCount)")
        } while (notionResponse.hasMore)

        logger.lifecycle("✅ Successfully fetched $pageCount pages from Notion")
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

        logger.lifecycle("📥 Received response body : ${body()}")

        val results = json[RESULTS]?.jsonArray ?: throw IllegalStateException("No results found")
        val nextCursor = json[NEXT_CURSOR]?.takeUnless { it.toString() == NULL }?.jsonPrimitive?.content
        val hasMore = json[HAS_MORE]?.jsonPrimitive?.content?.toBoolean() ?: false

        logger.lifecycle("🔍 Found ${results.size} raw results in response")

        val pages = results.mapNotNull { element ->
            val page = NotionPage.fromJsonElement(
                notionConfig = notionConfig,
                element = element,
                logger = logger
            )
            if (page == null) {
                logger.lifecycle("⚠️ Failed to parse page: ${element}")
            } else {
                logger.lifecycle("✅ Successfully parsed page: ${page.resourceId}")
            }
            page
        }

        logger.lifecycle("📋 Successfully parsed ${pages.size} pages out of ${results.size} raw results")

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
