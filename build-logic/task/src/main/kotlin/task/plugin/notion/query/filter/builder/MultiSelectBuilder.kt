package task.plugin.notion.query.filter.builder

import task.plugin.notion.query.filter.Condition

class MultiSelectBuilder {
    lateinit var property: String
    lateinit var condition: Condition.MultiSelect

    infix fun String.contains(value: String) {
        property = this
        condition = Condition.MultiSelect.Contains(value)
    }

    infix fun String.doesNotContain(value: String) {
        property = this
        condition = Condition.MultiSelect.DoesNotContain(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.MultiSelect.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.MultiSelect.IsNotEmpty
        }
}
