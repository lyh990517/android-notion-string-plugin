package com.yunho.notion.query.filter.builder

import com.yunho.notion.query.filter.Condition

class TimestampBuilder {
    lateinit var property: String
    lateinit var condition: Condition.Timestamp

    infix fun String.after(value: String) {
        property = this
        condition = Condition.Timestamp.After(value)
    }

    infix fun String.before(value: String) {
        property = this
        condition = Condition.Timestamp.Before(value)
    }

    infix fun String.equals(value: String) {
        property = this
        condition = Condition.Timestamp.Equals(value)
    }

    infix fun String.onOrAfter(value: String) {
        property = this
        condition = Condition.Timestamp.OnOrAfter(value)
    }

    infix fun String.onOrBefore(value: String) {
        property = this
        condition = Condition.Timestamp.OnOrBefore(value)
    }

    val String.nextMonth: Unit
        get() {
            property = this
            condition = Condition.Timestamp.NextMonth
        }

    val String.nextWeek: Unit
        get() {
            property = this
            condition = Condition.Timestamp.NextWeek
        }

    val String.nextYear: Unit
        get() {
            property = this
            condition = Condition.Timestamp.NextYear
        }

    val String.pastMonth: Unit
        get() {
            property = this
            condition = Condition.Timestamp.PastMonth
        }

    val String.pastWeek: Unit
        get() {
            property = this
            condition = Condition.Timestamp.PastWeek
        }

    val String.pastYear: Unit
        get() {
            property = this
            condition = Condition.Timestamp.PastYear
        }

    val String.thisWeek: Unit
        get() {
            property = this
            condition = Condition.Timestamp.ThisWeek
        }
}
