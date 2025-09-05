package task.plugin.notion.query.filter.builder

import task.plugin.notion.query.filter.Condition

class PhoneNumberBuilder {
    lateinit var property: String
    lateinit var condition: Condition.PhoneNumber

    infix fun String.equals(value: String) {
        property = this
        condition = Condition.PhoneNumber.Equals(value)
    }

    infix fun String.doesNotEqual(value: String) {
        property = this
        condition = Condition.PhoneNumber.DoesNotEqual(value)
    }

    infix fun String.contains(value: String) {
        property = this
        condition = Condition.PhoneNumber.Contains(value)
    }

    infix fun String.doesNotContain(value: String) {
        property = this
        condition = Condition.PhoneNumber.DoesNotContain(value)
    }

    infix fun String.startsWith(value: String) {
        property = this
        condition = Condition.PhoneNumber.StartsWith(value)
    }

    infix fun String.endsWith(value: String) {
        property = this
        condition = Condition.PhoneNumber.EndsWith(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.PhoneNumber.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.PhoneNumber.IsNotEmpty
        }
}
