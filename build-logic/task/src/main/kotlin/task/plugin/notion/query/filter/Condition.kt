package task.plugin.notion.query.filter

sealed interface Condition {
    sealed interface CheckBox : Condition {
        data class Equals(val value: Boolean) : CheckBox
        data class DoesNotEqual(val value: Boolean) : CheckBox
    }

    sealed interface Date : Condition {
        data class After(val value: String) : Date
        data class Before(val value: String) : Date
        data class Equals(val value: String) : Date
        data class OnOrAfter(val value: String) : Date
        data class OnOrBefore(val value: String) : Date
        object IsEmpty : Date
        object IsNotEmpty : Date
        object NextMonth : Date
        object NextWeek : Date
        object NextYear : Date
        object PastMonth : Date
        object PastWeek : Date
        object PastYear : Date
        object ThisWeek : Date
    }

    sealed interface Files : Condition {
        object IsEmpty : Files
        object IsNotEmpty : Files
    }

    sealed interface Formula : Condition {
        data class String(val condition: RichText) : Formula
        data class Number(val condition: Number) : Formula
        data class CheckBox(val condition: CheckBox) : Formula
        data class Date(val condition: Date) : Formula
    }

    sealed interface MultiSelect : Condition {
        data class Contains(val value: String) : MultiSelect
        data class DoesNotContain(val value: String) : MultiSelect
        object IsEmpty : MultiSelect
        object IsNotEmpty : MultiSelect
    }

    sealed interface Number : Condition {
        data class Equals(val value: kotlin.Number) : Number
        data class DoesNotEqual(val value: kotlin.Number) : Number
        data class GreaterThan(val value: kotlin.Number) : Number
        data class LessThan(val value: kotlin.Number) : Number
        data class GreaterThanOrEqualTo(val value: kotlin.Number) : Number
        data class LessThanOrEqualTo(val value: kotlin.Number) : Number
        object IsEmpty : Number
        object IsNotEmpty : Number
    }

    sealed interface People : Condition {
        data class Contains(val value: String) : People
        data class DoesNotContain(val value: String) : People
        object IsEmpty : People
        object IsNotEmpty : People
    }

    sealed interface PhoneNumber : Condition {
        data class Equals(val value: String) : PhoneNumber
        data class DoesNotEqual(val value: String) : PhoneNumber
        data class Contains(val value: String) : PhoneNumber
        data class DoesNotContain(val value: String) : PhoneNumber
        data class StartsWith(val value: String) : PhoneNumber
        data class EndsWith(val value: String) : PhoneNumber
        object IsEmpty : PhoneNumber
        object IsNotEmpty : PhoneNumber
    }

    sealed interface Relation : Condition {
        data class Contains(val value: String) : Relation
        data class DoesNotContain(val value: String) : Relation
        object IsEmpty : Relation
        object IsNotEmpty : Relation
    }

    sealed interface RichText : Condition {
        data class Equals(val value: String) : RichText
        data class DoesNotEqual(val value: String) : RichText
        data class Contains(val value: String) : RichText
        data class DoesNotContain(val value: String) : RichText
        data class StartsWith(val value: String) : RichText
        data class EndsWith(val value: String) : RichText
        object IsEmpty : RichText
        object IsNotEmpty : RichText
    }

    sealed interface Select : Condition {
        data class Equals(val value: String) : Select
        data class DoesNotEqual(val value: String) : Select
        object IsEmpty : Select
        object IsNotEmpty : Select
    }

    sealed interface Status : Condition {
        data class Equals(val value: String) : Status
        data class DoesNotEqual(val value: String) : Status
        object IsEmpty : Status
        object IsNotEmpty : Status
    }

    sealed interface Timestamp : Condition {
        data class After(val value: String) : Timestamp
        data class Before(val value: String) : Timestamp
        data class Equals(val value: String) : Timestamp
        data class OnOrAfter(val value: String) : Timestamp
        data class OnOrBefore(val value: String) : Timestamp
        object NextMonth : Timestamp
        object NextWeek : Timestamp
        object NextYear : Timestamp
        object PastMonth : Timestamp
        object PastWeek : Timestamp
        object PastYear : Timestamp
        object ThisWeek : Timestamp
    }

    sealed interface Verification : Condition {
        data class Equals(val value: State) : Verification
        data class DoesNotEqual(val value: State) : Verification
        object IsEmpty : Verification
        object IsNotEmpty : Verification

        enum class State {
            VERIFIED,
            UNVERIFIED
        }
    }

    sealed interface ID : Condition {
        data class Equals(val value: Int) : ID
        data class DoesNotEqual(val value: Int) : ID
        data class GreaterThan(val value: Int) : ID
        data class LessThan(val value: Int) : ID
        data class GreaterThanOrEqualTo(val value: Int) : ID
        data class LessThanOrEqualTo(val value: Int) : ID
    }

    companion object {
        fun Condition.toJson(): String = when (this) {
            is CheckBox.Equals -> """"checkbox": { "equals": $value }"""
            is CheckBox.DoesNotEqual -> """"checkbox": { "does_not_equal": $value }"""

            is Date.After -> """"date": { "after": "$value" }"""
            is Date.Before -> """"date": { "before": "$value" }"""
            is Date.Equals -> """"date": { "equals": "$value" }"""
            is Date.OnOrAfter -> """"date": { "on_or_after": "$value" }"""
            is Date.OnOrBefore -> """"date": { "on_or_before": "$value" }"""
            Date.IsEmpty -> """"date": { "is_empty": true }"""
            Date.IsNotEmpty -> """"date": { "is_not_empty": true }"""
            Date.NextMonth -> """"date": { "next_month": {} }"""
            Date.NextWeek -> """"date": { "next_week": {} }"""
            Date.NextYear -> """"date": { "next_year": {} }"""
            Date.PastMonth -> """"date": { "past_month": {} }"""
            Date.PastWeek -> """"date": { "past_week": {} }"""
            Date.PastYear -> """"date": { "past_year": {} }"""
            Date.ThisWeek -> """"date": { "this_week": {} }"""

            Files.IsEmpty -> """"files": { "is_empty": true }"""
            Files.IsNotEmpty -> """"files": { "is_not_empty": true }"""

            is MultiSelect.Contains -> """"multi_select": { "contains": "$value" }"""
            is MultiSelect.DoesNotContain -> """"multi_select": { "does_not_contain": "$value" }"""
            MultiSelect.IsEmpty -> """"multi_select": { "is_empty": true }"""
            MultiSelect.IsNotEmpty -> """"multi_select": { "is_not_empty": true }"""

            is Number.Equals -> """"number": { "equals": $value }"""
            is Number.DoesNotEqual -> """"number": { "does_not_equal": $value }"""
            is Number.GreaterThan -> """"number": { "greater_than": $value }"""
            is Number.LessThan -> """"number": { "less_than": $value }"""
            is Number.GreaterThanOrEqualTo -> """"number": { "greater_than_or_equal_to": $value }"""
            is Number.LessThanOrEqualTo -> """"number": { "less_than_or_equal_to": $value }"""
            Number.IsEmpty -> """"number": { "is_empty": true }"""
            Number.IsNotEmpty -> """"number": { "is_not_empty": true }"""

            is People.Contains -> """"people": { "contains": "$value" }"""
            is People.DoesNotContain -> """"people": { "does_not_contain": "$value" }"""
            People.IsEmpty -> """"people": { "is_empty": true }"""
            People.IsNotEmpty -> """"people": { "is_not_empty": true }"""

            is PhoneNumber.Equals -> """"phone_number": { "equals": "$value" }"""
            is PhoneNumber.DoesNotEqual -> """"phone_number": { "does_not_equal": "$value" }"""
            is PhoneNumber.Contains -> """"phone_number": { "contains": "$value" }"""
            is PhoneNumber.DoesNotContain -> """"phone_number": { "does_not_contain": "$value" }"""
            is PhoneNumber.StartsWith -> """"phone_number": { "starts_with": "$value" }"""
            is PhoneNumber.EndsWith -> """"phone_number": { "ends_with": "$value" }"""
            PhoneNumber.IsEmpty -> """"phone_number": { "is_empty": true }"""
            PhoneNumber.IsNotEmpty -> """"phone_number": { "is_not_empty": true }"""

            is Relation.Contains -> """"relation": { "contains": "$value" }"""
            is Relation.DoesNotContain -> """"relation": { "does_not_contain": "$value" }"""
            Relation.IsEmpty -> """"relation": { "is_empty": true }"""
            Relation.IsNotEmpty -> """"relation": { "is_not_empty": true }"""

            is RichText.Equals -> """"rich_text": { "equals": "$value" }"""
            is RichText.DoesNotEqual -> """"rich_text": { "does_not_equal": "$value" }"""
            is RichText.Contains -> """"rich_text": { "contains": "$value" }"""
            is RichText.DoesNotContain -> """"rich_text": { "does_not_contain": "$value" }"""
            is RichText.StartsWith -> """"rich_text": { "starts_with": "$value" }"""
            is RichText.EndsWith -> """"rich_text": { "ends_with": "$value" }"""
            RichText.IsEmpty -> """"rich_text": { "is_empty": true }"""
            RichText.IsNotEmpty -> """"rich_text": { "is_not_empty": true }"""

            is Select.Equals -> """"select": { "equals": "$value" }"""
            is Select.DoesNotEqual -> """"select": { "does_not_equal": "$value" }"""
            Select.IsEmpty -> """"select": { "is_empty": true }"""
            Select.IsNotEmpty -> """"select": { "is_not_empty": true }"""

            is Status.Equals -> """"status": { "equals": "$value" }"""
            is Status.DoesNotEqual -> """"status": { "does_not_equal": "$value" }"""
            Status.IsEmpty -> """"status": { "is_empty": true }"""
            Status.IsNotEmpty -> """"status": { "is_not_empty": true }"""

            is Timestamp.After -> """"timestamp": { "after": "$value" }"""
            is Timestamp.Before -> """"timestamp": { "before": "$value" }"""
            is Timestamp.Equals -> """"timestamp": { "equals": "$value" }"""
            is Timestamp.OnOrAfter -> """"timestamp": { "on_or_after": "$value" }"""
            is Timestamp.OnOrBefore -> """"timestamp": { "on_or_before": "$value" }"""
            Timestamp.NextMonth -> """"timestamp": { "next_month": {} }"""
            Timestamp.NextWeek -> """"timestamp": { "next_week": {} }"""
            Timestamp.NextYear -> """"timestamp": { "next_year": {} }"""
            Timestamp.PastMonth -> """"timestamp": { "past_month": {} }"""
            Timestamp.PastWeek -> """"timestamp": { "past_week": {} }"""
            Timestamp.PastYear -> """"timestamp": { "past_year": {} }"""
            Timestamp.ThisWeek -> """"timestamp": { "this_week": {} }"""

            is Verification.Equals -> """"verification": { "equals": "${value.name}" }"""
            is Verification.DoesNotEqual -> """"verification": { "does_not_equal": "${value.name}" }"""
            Verification.IsEmpty -> """"verification": { "is_empty": true }"""
            Verification.IsNotEmpty -> """"verification": { "is_not_empty": true }"""

            is ID.Equals -> """"id": { "equals": $value }"""
            is ID.DoesNotEqual -> """"id": { "does_not_equal": $value }"""
            is ID.GreaterThan -> """"id": { "greater_than": $value }"""
            is ID.LessThan -> """"id": { "less_than": $value }"""
            is ID.GreaterThanOrEqualTo -> """"id": { "greater_than_or_equal_to": $value }"""
            is ID.LessThanOrEqualTo -> """"id": { "less_than_or_equal_to": $value }"""

            is Formula -> when (this) {
                is Formula.String -> """"formula": { ${condition.toJson()} }"""
                is Formula.Number -> """"formula": { ${condition.toJson()} }"""
                is Formula.CheckBox -> """"formula": { ${condition.toJson()} }"""
                is Formula.Date -> """"formula": { ${condition.toJson()} }"""
            }
        }
    }
}
