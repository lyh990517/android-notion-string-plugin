package com.yunho.notion.task

import com.google.gson.JsonArray
import com.google.gson.JsonParser
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
            val json = JsonParser.parseString(body).asJsonObject
            val results = json.getAsJsonArray(RESULTS)
            val nextCursor = json.get(NEXT_CURSOR)?.takeIf { !it.isJsonNull }?.asString
            val hasMore = json.get(HAS_MORE)?.asBoolean ?: false

            return NotionDatabaseResponse(
                results = results,
                nextCursor = nextCursor,
                hasMore = hasMore
            )
        }
    }
}
