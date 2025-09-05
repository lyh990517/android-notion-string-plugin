package com.yunho.notion.query.filter.builder

import com.yunho.notion.query.filter.Condition

class RichTextBuilder {
    lateinit var property: String
    lateinit var condition: Condition.RichText

    infix fun String.contains(value: String) {
        property = this
        condition = Condition.RichText.Contains(value)
    }

    infix fun String.equals(value: String) {
        property = this
        condition = Condition.RichText.Equals(value)
    }

    infix fun String.doesNotContain(value: String) {
        property = this
        condition = Condition.RichText.DoesNotContain(value)
    }

    infix fun String.doesNotEqual(value: String) {
        property = this
        condition = Condition.RichText.DoesNotEqual(value)
    }

    infix fun String.startsWith(value: String) {
        property = this
        condition = Condition.RichText.StartsWith(value)
    }

    infix fun String.endsWith(value: String) {
        property = this
        condition = Condition.RichText.EndsWith(value)
    }

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.RichText.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.RichText.IsNotEmpty
        }
}
