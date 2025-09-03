package com.yunho.notion.task

import java.io.File

object StringResourceGenerator {
    private const val STRINGS_XML = "strings.xml"
    private const val XML_HEADER = """<?xml version="1.0" encoding="utf-8"?>"""
    private const val RESOURCES_OPEN = """<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">"""
    private const val RESOURCES_CLOSE = """</resources>"""

    fun generateAll(pageData: List<NotionPageData>, outputPath: String) {
        Language.values().forEach { language ->
            generateForLanguage(pageData, language, outputPath)
        }
    }

    private fun generateForLanguage(
        pageData: List<NotionPageData>,
        language: Language,
        outputPath: String
    ) {
        val directory = File(outputPath, language.resDir).apply { mkdirs() }
        val outputFile = File(directory, STRINGS_XML)

        outputFile.bufferedWriter().use { writer ->
            writer.appendLine(XML_HEADER)
            writer.appendLine(RESOURCES_OPEN)

            pageData.forEach { page ->
                val translation = page.translations[language].orEmpty()

                if (translation.isNotBlank()) {
                    writer.appendLine(createStringEntry(page.resourceId, translation))
                }
            }

            writer.appendLine(RESOURCES_CLOSE)
        }

        println("✅ Generated ${language.name} → ${directory.relativeTo(File(outputPath)).path}")
    }

    private fun createStringEntry(resourceId: String, content: String): String {
        return """    <string name="$resourceId">$content</string>"""
    }
}
