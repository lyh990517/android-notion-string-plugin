package com.yunho.notion.task

enum class Language(
    val notionColumn: String,
    val resDir: String
) {
    KOR(
        notionColumn = "String: KOR",
        resDir = "values-ko"
    ),
    JPN(
        notionColumn = "String: JPN",
        resDir = "values-ja"
    ),
    ENG(
        notionColumn = "String: BASE",
        resDir = "values"
    )
}
