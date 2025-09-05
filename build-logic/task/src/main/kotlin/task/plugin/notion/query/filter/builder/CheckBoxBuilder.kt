package task.plugin.notion.query.filter.builder

import task.plugin.notion.query.filter.Condition

class CheckBoxBuilder {
    lateinit var property: String
    lateinit var condition: Condition.CheckBox

    infix fun String.equals(value: Boolean) {
        property = this
        condition = Condition.CheckBox.Equals(value)
    }

    infix fun String.doesNotEqual(value: Boolean) {
        property = this
        condition = Condition.CheckBox.DoesNotEqual(value)
    }
}
