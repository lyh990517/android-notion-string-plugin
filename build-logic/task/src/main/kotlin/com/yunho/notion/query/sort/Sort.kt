package com.yunho.notion.query.sort

import com.yunho.notion.query.sort.Timestamp as TimestampType

sealed interface Sort {
    data class Property(val property: String, val direction: Direction) : Sort
    data class Timestamp(val timestamp: TimestampType, val direction: Direction) : Sort

    companion object {
        fun Sort.toJson(): String = when (this) {
            is Property -> """
            {
              "property": "$property",
              "direction": "${direction.value}"
            }
        """.trimIndent()

            is Timestamp -> """
            {
              "timestamp": "${timestamp.value}",
              "direction": "${direction.value}"
            }
        """.trimIndent()
        }
    }
}
