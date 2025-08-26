package com.yunho.notion.task

import java.io.File

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
    );

    companion object {
        fun createDir(baseDir: File): Map<Language, File> {
            return values().associateWith { language ->
                File(baseDir, language.resDir)
            }
        }
    }
}
