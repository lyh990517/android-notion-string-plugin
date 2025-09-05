package com.yunho.notion.query.filter

import com.yunho.notion.query.filter.Condition.Companion.toJson

sealed interface Filter {
    data class Property(
        val property: String,
        val condition: Condition
    ) : Filter

    data class And(val filters: List<Filter>) : Filter
    data class Or(val filters: List<Filter>) : Filter

    companion object {
        fun Filter.toJson(): String = when (this) {
            is Property -> """
            {
              "property": "$property",
              ${condition.toJson()}
            }
        """.trimIndent()

            is And -> """
            {
              "and": [
                ${filters.joinToString(",\n") { it.toJson().prependIndent("    ") }}
              ]
            }
        """.trimIndent()

            is Or -> """
            {
              "or": [
                ${filters.joinToString(",\n") { it.toJson().prependIndent("    ") }}
              ]
            }
        """.trimIndent()
        }
    }
}
