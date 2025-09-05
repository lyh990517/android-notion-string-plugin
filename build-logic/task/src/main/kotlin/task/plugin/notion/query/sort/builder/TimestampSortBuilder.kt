package task.plugin.notion.query.sort.builder

import task.plugin.notion.query.sort.Direction
import task.plugin.notion.query.sort.Timestamp

class TimestampSortBuilder {
    lateinit var timestamp: Timestamp
    var direction: Direction = Direction.ASCENDING

    infix fun Timestamp.by(direction: Direction) {
        timestamp = this
        this@TimestampSortBuilder.direction = direction
    }
}
