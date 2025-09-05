package com.yunho.notion.query.filter.builder

import com.yunho.notion.query.filter.Condition

class StatusBuilder {
    lateinit var property: String
    lateinit var condition: Condition.Status

    infix fun String.equals(value: String) {
        property = this
        condition = Condition.Status.Equals(value)
    }

    infix fun String.doesNotEqual(value: String) {
        property = this
        condition = Condition.Status.DoesNotEqual(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.Status.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.Status.IsNotEmpty
        }
}
