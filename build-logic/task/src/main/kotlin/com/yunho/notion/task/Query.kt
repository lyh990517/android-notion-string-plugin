package com.yunho.notion.task

enum class Query(val value: String) {
    CONTAINS("contains"),
    EQUALS("equals"),
    DOES_NOT_CONTAIN("does_not_contain"),
    DOES_NOT_EQUAL("does_not_equal")
}
