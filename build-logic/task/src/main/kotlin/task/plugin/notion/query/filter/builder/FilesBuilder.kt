package task.plugin.notion.query.filter.builder

import task.plugin.notion.query.filter.Condition

class FilesBuilder {
    lateinit var property: String
    lateinit var condition: Condition.Files

    val String.isEmpty: Unit
        get() {
            property = this
            condition = Condition.Files.IsEmpty
        }

    val String.isNotEmpty: Unit
        get() {
            property = this
            condition = Condition.Files.IsNotEmpty
        }
}
