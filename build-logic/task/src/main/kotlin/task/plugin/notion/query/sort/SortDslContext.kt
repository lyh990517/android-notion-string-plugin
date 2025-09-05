package task.plugin.notion.query.sort

import task.plugin.notion.query.sort.builder.PropertySortBuilder
import task.plugin.notion.query.sort.builder.TimestampSortBuilder

class SortDslContext {
    val sorts = mutableListOf<Sort>()

    fun property(block: PropertySortBuilder.() -> Unit) {
        val builder = PropertySortBuilder()
        builder.block()
        sorts.add(Sort.Property(builder.property, builder.direction))
    }

    fun timestamp(block: TimestampSortBuilder.() -> Unit) {
        val builder = TimestampSortBuilder()
        builder.block()
        sorts.add(Sort.Timestamp(builder.timestamp, builder.direction))
    }
}
