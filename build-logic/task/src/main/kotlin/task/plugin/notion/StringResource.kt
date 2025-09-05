package task.plugin.notion

import task.plugin.notion.model.Language
import task.plugin.notion.model.NotionPage
import java.io.File

object StringResource {
    private const val STRINGS_XML = "strings.xml"
    private const val XML_HEADER = """<?xml version="1.0" encoding="utf-8"?>"""
    private const val RESOURCES_OPEN = """<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">"""
    private const val RESOURCES_CLOSE = """</resources>"""

    fun create(
        pages: List<NotionPage>,
        outputDir: String
    ) {
        Language.values().forEach { language ->
            createForLanguage(
                pages = pages,
                language = language,
                outputDir = outputDir
            )
        }
    }

    private fun createForLanguage(
        pages: List<NotionPage>,
        language: Language,
        outputDir: String
    ) {
        val directory = File(outputDir, language.resDir).apply { mkdirs() }
        val outputFile = File(directory, STRINGS_XML)

        outputFile.bufferedWriter().use { writer ->
            writer.appendLine(XML_HEADER)
            writer.appendLine(RESOURCES_OPEN)

            pages.forEach { page ->
                with(page) {
                    writer.appendLine("""    <string name="$resourceId">${translations[language].orEmpty()}</string>""")
                }
            }

            writer.appendLine(RESOURCES_CLOSE)
        }

        println("✅ Generated ${language.name} → ${directory.relativeTo(File(outputDir)).path}")
    }

}
