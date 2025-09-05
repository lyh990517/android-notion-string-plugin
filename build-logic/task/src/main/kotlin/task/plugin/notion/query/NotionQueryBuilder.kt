package task.plugin.notion.query

import task.plugin.notion.query.filter.Filter
import task.plugin.notion.query.filter.Filter.Companion.toJson
import task.plugin.notion.query.filter.FilterDslContext
import task.plugin.notion.query.sort.Sort
import task.plugin.notion.query.sort.Sort.Companion.toJson
import task.plugin.notion.query.sort.SortDslContext
import kotlin.collections.isNotEmpty
import kotlin.collections.joinToString

class NotionQueryBuilder {
    private lateinit var filter: Filter
    private lateinit var sorts : List<Sort>
    private var cursor: String? = null

    fun filter(block: FilterDslContext.() -> Filter): NotionQueryBuilder {
        val dsl = FilterDslContext()
        val filter = dsl.block()
        this.filter = filter
        return this
    }

    fun sort(block: SortDslContext.() -> Unit): NotionQueryBuilder {
        val dsl = SortDslContext()
        dsl.block()
        sorts = dsl.sorts
        return this
    }

    fun cursor(cursor: String?): NotionQueryBuilder {
        this.cursor = cursor
        return this
    }

    fun build(): String {
        val filterJson = """"filter": ${filter.toJson()},"""
        val sortsJson = if (sorts.isNotEmpty()) {
            """"sorts": [${sorts.joinToString(",") { it.toJson() }}],"""
        } else ""

        val cursorJson = cursor?.let { """"start_cursor": "$it",""" } ?: ""

        return """
        {
          $filterJson
          $sortsJson
          $cursorJson
          "page_size": $PAGE_SIZE
        }
        """.trimIndent().replace(Regex(",\\s*}"), " }").replace(Regex("\\n\\s*\\n"), "\n")
    }

    companion object {
        private const val PAGE_SIZE = 100
    }
}
