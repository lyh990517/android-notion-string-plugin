package com.yunho.notion.task

class NotionQueryBuilder {
    private val filters = mutableListOf<FilterCondition>()

    fun reset(): NotionQueryBuilder {
        filters.clear()
        return this
    }

    fun addRichText(property: String, query: Query, value: String): NotionQueryBuilder {
        filters.add(FilterCondition.richText(property, query, value))
        return this
    }

    fun addSelect(property: String, query: Query, value: String): NotionQueryBuilder {
        filters.add(FilterCondition.select(property, query, value))
        return this
    }

    fun addMultiSelect(property: String, query: Query, value: String): NotionQueryBuilder {
        filters.add(FilterCondition.multiSelect(property, query, value))
        return this
    }

    fun addStatus(property: String, query: Query, value: String): NotionQueryBuilder {
        filters.add(FilterCondition.status(property, query, value))
        return this
    }

    fun build(pageSize: Int = 100, startCursor: String? = null): String {
        val filterJson = if (filters.isEmpty()) {
            ""
        } else {
            """
            "filter": {
              "and": [
            ${filters.joinToString(",\n") { it.toJson().prependIndent("    ") }}
              ]
            },""".trimIndent()
        }

        val cursorJson = startCursor?.let {
            """"start_cursor": "$it","""
        } ?: ""

        return """
        {
          $filterJson
          $cursorJson
          "page_size": $pageSize
        }
        """.trimIndent().replace(Regex("\\n\\s*\\n"), "\n")
    }

    private data class FilterCondition(
        val property: String,
        val type: String,
        val operation: String,
        val value: String
    ) {
        fun toJson(): String = """
        {
          "property": "$property",
          "$type": { "$operation": ${value.toJsonString()} }
        }
        """.trimIndent()

        private fun String.toJsonString(): String = "\"${replace("\"", "\\\"")}\""

        companion object {
            fun richText(property: String, query: Query, value: String) =
                FilterCondition(property, "rich_text", query.value, value)

            fun select(property: String, query: Query, value: String) =
                FilterCondition(property, "select", query.value, value)

            fun multiSelect(property: String, query: Query, value: String) =
                FilterCondition(property, "multi_select", query.value, value)

            fun status(property: String, query: Query, value: String) =
                FilterCondition(property, "status", query.value, value)
        }
    }
}
