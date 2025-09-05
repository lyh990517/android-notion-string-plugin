package task.plugin.notion.query.filter.builder

import task.plugin.notion.query.filter.Condition

class VerificationBuilder {
    lateinit var property: String
    lateinit var condition: Condition.Verification

    infix fun String.equals(value: Condition.Verification.State) {
        property = this
        condition = Condition.Verification.Equals(value)
    }

    infix fun String.doesNotEqual(value: Condition.Verification.State) {
        property = this
        condition = Condition.Verification.DoesNotEqual(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.Verification.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.Verification.IsNotEmpty
        }
}
