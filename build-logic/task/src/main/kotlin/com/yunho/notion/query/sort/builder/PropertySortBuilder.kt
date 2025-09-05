package com.yunho.notion.query.sort.builder

import com.yunho.notion.query.sort.Direction

class PropertySortBuilder {
    lateinit var property: String
    var direction: Direction = Direction.ASCENDING

    infix fun String.by(direction: Direction) {
        property = this
        this@PropertySortBuilder.direction = direction
    }
}
