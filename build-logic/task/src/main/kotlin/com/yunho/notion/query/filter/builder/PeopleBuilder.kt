package com.yunho.notion.query.filter.builder

import com.yunho.notion.query.filter.Condition

class PeopleBuilder {
    lateinit var property: String
    lateinit var condition: Condition.People

    infix fun String.contains(value: String) {
        property = this
        condition = Condition.People.Contains(value)
    }

    infix fun String.doesNotContain(value: String) {
        property = this
        condition = Condition.People.DoesNotContain(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.People.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.People.IsNotEmpty
        }
}
