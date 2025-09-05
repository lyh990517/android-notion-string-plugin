package task.plugin.notion.query.filter.builder

import task.plugin.notion.query.filter.Condition

class FormulaBuilder {
    lateinit var property: String
    lateinit var condition: Condition.Formula

    infix fun String.stringCondition(condition: Condition.RichText) {
        property = this
        this@FormulaBuilder.condition = Condition.Formula.String(condition)
    }

    infix fun String.numberCondition(condition: Condition.Formula.Number) {
        property = this
        this@FormulaBuilder.condition = Condition.Formula.Number(condition)
    }

    infix fun String.checkBoxCondition(condition: Condition.Formula.CheckBox) {
        property = this
        this@FormulaBuilder.condition = Condition.Formula.CheckBox(condition)
    }

    infix fun String.dateCondition(condition: Condition.Formula.Date) {
        property = this
        this@FormulaBuilder.condition = Condition.Formula.Date(condition)
    }
}
