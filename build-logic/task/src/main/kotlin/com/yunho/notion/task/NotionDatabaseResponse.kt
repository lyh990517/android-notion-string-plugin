package com.yunho.notion.task

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.http.HttpResponse

data class NotionDatabaseResponse(
    val results: JsonArray,
    val nextCursor: String?,
    val hasMore: Boolean
) {
    companion object {
        private const val RESULTS = "results"
        private const val NEXT_CURSOR = "next_cursor"
        private const val HAS_MORE = "has_more"

        fun create(response: HttpResponse<String>): NotionDatabaseResponse {
            val body = response.body()
            val json = Json.parseToJsonElement(body).jsonObject
            val results = json[RESULTS]!!.jsonArray
            val nextCursor = json[NEXT_CURSOR]?.takeIf { !it.toString().equals("null", true) }?.jsonPrimitive?.content
            val hasMore = json[HAS_MORE]?.jsonPrimitive?.content?.toBoolean() ?: false

            return NotionDatabaseResponse(
                results = results,
                nextCursor = nextCursor,
                hasMore = hasMore
            )
        }
    }
}
