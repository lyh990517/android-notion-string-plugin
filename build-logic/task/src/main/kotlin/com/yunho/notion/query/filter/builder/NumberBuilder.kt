package com.yunho.notion.query.filter.builder

import com.yunho.notion.query.filter.Condition

class NumberBuilder {
    lateinit var property: String
    lateinit var condition: Condition.Number

    infix fun String.equals(value: Number) {
        property = this
        condition = Condition.Number.Equals(value)
    }

    infix fun String.doesNotEqual(value: Number) {
        property = this
        condition = Condition.Number.DoesNotEqual(value)
    }

    infix fun String.greaterThan(value: Number) {
        property = this
        condition = Condition.Number.GreaterThan(value)
    }

    infix fun String.lessThan(value: Number) {
        property = this
        condition = Condition.Number.LessThan(value)
    }

    infix fun String.greaterThanOrEqualTo(value: Number) {
        property = this
        condition = Condition.Number.GreaterThanOrEqualTo(value)
    }

    infix fun String.lessThanOrEqualTo(value: Number) {
        property = this
        condition = Condition.Number.LessThanOrEqualTo(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.Number.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.Number.IsNotEmpty
        }
}
