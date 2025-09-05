package com.yunho.notion.model

data class NotionResponse(
    val pages: List<NotionPage>,
    val nextCursor: String?,
    val hasMore: Boolean
)
