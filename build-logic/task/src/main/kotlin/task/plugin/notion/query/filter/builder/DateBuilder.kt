package task.plugin.notion.query.filter.builder

import task.plugin.notion.query.filter.Condition

class DateBuilder {
    lateinit var property: String
    lateinit var condition: Condition.Date

    infix fun String.after(value: String) {
        property = this
        condition = Condition.Date.After(value)
    }

    infix fun String.before(value: String) {
        property = this
        condition = Condition.Date.Before(value)
    }

    infix fun String.equals(value: String) {
        property = this
        condition = Condition.Date.Equals(value)
    }

    infix fun String.onOrAfter(value: String) {
        property = this
        condition = Condition.Date.OnOrAfter(value)
    }

    infix fun String.onOrBefore(value: String) {
        property = this
        condition = Condition.Date.OnOrBefore(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.Date.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.Date.IsNotEmpty
        }

    val String.nextMonth: Unit
        get() {
            property = this
            condition = Condition.Date.NextMonth
        }

    val String.nextWeek: Unit
        get() {
            property = this
            condition = Condition.Date.NextWeek
        }

    val String.nextYear: Unit
        get() {
            property = this
            condition = Condition.Date.NextYear
        }

    val String.pastMonth: Unit
        get() {
            property = this
            condition = Condition.Date.PastMonth
        }

    val String.pastWeek: Unit
        get() {
            property = this
            condition = Condition.Date.PastWeek
        }

    val String.pastYear: Unit
        get() {
            property = this
            condition = Condition.Date.PastYear
        }

    val String.thisWeek: Unit
        get() {
            property = this
            condition = Condition.Date.ThisWeek
        }
}
