package task.plugin.notion

import task.plugin.notion.model.Language
import task.plugin.notion.model.NotionPage
import java.io.File
import org.gradle.api.logging.Logger

class StringResource(
    private val notionConfig: NotionConfig,
    private val logger: Logger
) {
    fun create(
        pages: List<NotionPage>,
    ) {
        logger.lifecycle("📝 Generating string resources for ${pages.size} pages")

        if (pages.isEmpty()) {
            logger.lifecycle("⚠️ No pages to process, skipping string resource generation")
            return
        }

        with(notionConfig) {
            languages.forEach { language ->
                createForLanguage(
                    pages = pages,
                    language = language,
                    outputDir = outputDir
                )
            }
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
                    val translation = translations[language].orEmpty()
                    writer.appendLine("""    <string name="$resourceId">$translation</string>""")
                }
            }

            writer.appendLine(RESOURCES_CLOSE)
        }

        logger.lifecycle("✅ Generated ${language.javaClass.simpleName} → ${directory.relativeTo(File(outputDir)).path}")
    }

    companion object {
        private const val STRINGS_XML = "strings.xml"
        private const val XML_HEADER = """<?xml version="1.0" encoding="utf-8"?>"""
        private const val RESOURCES_OPEN = """<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">"""
        private const val RESOURCES_CLOSE = """</resources>"""
    }
}
