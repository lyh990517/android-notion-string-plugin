package task.plugin.notion.query.filter.builder

import task.plugin.notion.query.filter.Condition

class RelationBuilder {
    lateinit var property: String
    lateinit var condition: Condition.Relation

    infix fun String.contains(value: String) {
        property = this
        condition = Condition.Relation.Contains(value)
    }

    infix fun String.doesNotContain(value: String) {
        property = this
        condition = Condition.Relation.DoesNotContain(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.Relation.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.Relation.IsNotEmpty
        }
}
