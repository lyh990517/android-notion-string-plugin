package com.yunho.notion.query.filter.builder

import com.yunho.notion.query.filter.Condition

class IDBuilder {
    lateinit var property: String
    lateinit var condition: Condition.ID

    infix fun String.equals(value: Int) {
        property = this
        condition = Condition.ID.Equals(value)
    }

    infix fun String.doesNotEqual(value: Int) {
        property = this
        condition = Condition.ID.DoesNotEqual(value)
    }

    infix fun String.greaterThan(value: Int) {
        property = this
        condition = Condition.ID.GreaterThan(value)
    }

    infix fun String.lessThan(value: Int) {
        property = this
        condition = Condition.ID.LessThan(value)
    }

    infix fun String.greaterThanOrEqualTo(value: Int) {
        property = this
        condition = Condition.ID.GreaterThanOrEqualTo(value)
    }

    infix fun String.lessThanOrEqualTo(value: Int) {
        property = this
        condition = Condition.ID.LessThanOrEqualTo(value)
    }
}
