package task.plugin.notion.query.filter.builder

import task.plugin.notion.query.filter.Condition

class SelectBuilder {
    lateinit var property: String
    lateinit var condition: Condition.Select

    infix fun String.equals(value: String) {
        property = this
        condition = Condition.Select.Equals(value)
    }

    infix fun String.doesNotEqual(value: String) {
        property = this
        condition = Condition.Select.DoesNotEqual(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.Select.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.Select.IsNotEmpty
        }
}
