package task.plugin.notion.query.filter

import task.plugin.notion.query.filter.builder.CheckBoxBuilder
import task.plugin.notion.query.filter.builder.DateBuilder
import task.plugin.notion.query.filter.builder.FilesBuilder
import task.plugin.notion.query.filter.builder.FormulaBuilder
import task.plugin.notion.query.filter.builder.IDBuilder
import task.plugin.notion.query.filter.builder.MultiSelectBuilder
import task.plugin.notion.query.filter.builder.NumberBuilder
import task.plugin.notion.query.filter.builder.PeopleBuilder
import task.plugin.notion.query.filter.builder.PhoneNumberBuilder
import task.plugin.notion.query.filter.builder.RelationBuilder
import task.plugin.notion.query.filter.builder.RichTextBuilder
import task.plugin.notion.query.filter.builder.SelectBuilder
import task.plugin.notion.query.filter.builder.StatusBuilder
import task.plugin.notion.query.filter.builder.TimestampBuilder
import task.plugin.notion.query.filter.builder.VerificationBuilder

class FilterDslContext {
    fun select(block: SelectBuilder.() -> Unit): Filter {
        val builder = SelectBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun multiSelect(block: MultiSelectBuilder.() -> Unit): Filter {
        val builder = MultiSelectBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun checkBox(block: CheckBoxBuilder.() -> Unit): Filter {
        val builder = CheckBoxBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun richText(block: RichTextBuilder.() -> Unit): Filter {
        val builder = RichTextBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun date(block: DateBuilder.() -> Unit): Filter {
        val builder = DateBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun number(block: NumberBuilder.() -> Unit): Filter {
        val builder = NumberBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun files(block: FilesBuilder.() -> Unit): Filter {
        val builder = FilesBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun formula(block: FormulaBuilder.() -> Unit): Filter {
        val builder = FormulaBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun people(block: PeopleBuilder.() -> Unit): Filter {
        val builder = PeopleBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun phoneNumber(block: PhoneNumberBuilder.() -> Unit): Filter {
        val builder = PhoneNumberBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun relation(block: RelationBuilder.() -> Unit): Filter {
        val builder = RelationBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun status(block: StatusBuilder.() -> Unit): Filter {
        val builder = StatusBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun timestamp(block: TimestampBuilder.() -> Unit): Filter {
        val builder = TimestampBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun verification(block: VerificationBuilder.() -> Unit): Filter {
        val builder = VerificationBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun id(block: IDBuilder.() -> Unit): Filter {
        val builder = IDBuilder()
        builder.block()
        return Filter.Property(builder.property, builder.condition)
    }

    fun or(block: FilterDslContext.() -> List<Filter>): Filter {
        val filters = block()
        return Filter.Or(filters)
    }

    fun and(block: FilterDslContext.() -> List<Filter>): Filter {
        val filters = block()
        return Filter.And(filters)
    }

    infix fun Filter.and(other: Filter): Filter {
        val leftFilters = when (this) {
            is Filter.And -> this.filters
            else -> listOf(this)
        }
        val rightFilters = when (other) {
            is Filter.And -> other.filters
            else -> listOf(other)
        }
        return Filter.And(leftFilters + rightFilters)
    }

    infix fun Filter.or(other: Filter): Filter {
        val leftFilters = when (this) {
            is Filter.Or -> this.filters
            else -> listOf(this)
        }
        val rightFilters = when (other) {
            is Filter.Or -> other.filters
            else -> listOf(other)
        }
        return Filter.Or(leftFilters + rightFilters)
    }
}
