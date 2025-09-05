package com.yunho.notion.query.sort.builder

import com.yunho.notion.query.sort.Direction
import com.yunho.notion.query.sort.Timestamp

class TimestampSortBuilder {
    lateinit var timestamp: Timestamp
    var direction: Direction = Direction.ASCENDING

    infix fun Timestamp.by(direction: Direction) {
        timestamp = this
        this@TimestampSortBuilder.direction = direction
    }
}
