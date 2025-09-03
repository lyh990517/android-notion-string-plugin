package com.yunho.notion.task

data class NotionResponse(
    val pages: List<NotionPageData>,
    val nextCursor: String?,
    val hasMore: Boolean
)
