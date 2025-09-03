package com.yunho.notion.task

class NotionQueryBuilder {
    private val filters = mutableListOf<String>()

    fun reset(): NotionQueryBuilder {
        filters.clear()
        return this
    }

    fun addRichText(property: String, query: Query, value: String): NotionQueryBuilder {
        filters += """
          {
            "property": "$property",
            "rich_text": { "${query.value}" : ${jsonString(value)} }
          }
        """.trimIndent()
        return this
    }

    fun addSelect(property: String, query: Query, value: String): NotionQueryBuilder {
        filters += """
          {
            "property": "$property",
            "select": { "${query.value}" : ${jsonString(value)} }
          }
        """.trimIndent()
        return this
    }

    fun addMultiSelect(property: String, query: Query, value: String): NotionQueryBuilder {
        filters += """
          {
            "property": "$property",
            "multi_select": { "${query.value}" : ${jsonString(value)} }
          }
        """.trimIndent()
        return this
    }

    fun addStatus(property: String, query: Query, value: String): NotionQueryBuilder {
        filters += """
          {
            "property": "$property",
            "status": { "${query.value}" : ${jsonString(value)} }
          }
        """.trimIndent()
        return this
    }

    fun build(pageSize: Int = 100, startCursor: String? = null): String {
        val joined = filters.joinToString(",\n") { it.prependIndent("        ") }

        val cursorPart = if (startCursor != null) """,
        "start_cursor": "$startCursor"""" else ""

        return """
        {
          "filter": {
            "and": [
                $joined
            ]
          },
          "page_size": $pageSize$cursorPart
        }
        """.trimIndent()
    }

    private fun jsonString(s: String): String =
        "\"${s.replace("\"", "\\\"")}\""
}
