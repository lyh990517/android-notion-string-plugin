package com.yunho.notion.task

object Key {
    const val TYPE = "type"
    const val TITLE = "title"
    const val RICH_TEXT = "rich_text"
    const val FORMULA = "formula"
    const val STRING = "string"
    const val SELECT = "select"
    const val MULTI_SELECT = "multi_select"
    const val NAME = "name"
    const val PLAIN_TEXT = "plain_text"
    const val PROPERTIES = "properties"

    const val AMPERSAND = "&"
    const val AMPERSAND_ESCAPED = "&amp;"
    const val LESS_THAN = "<"
    const val LESS_THAN_ESCAPED = "&lt;"
    const val GREATER_THAN = ">"
    const val GREATER_THAN_ESCAPED = "&gt;"
    const val QUOTE = "\""
    const val QUOTE_ESCAPED = "&quot;"
    const val APOSTROPHE = "'"
    const val APOSTROPHE_ESCAPED = "\\'"

    const val PLACEHOLDER_REGEX = """\{([^}]+)\}"""
    const val PLACEHOLDER_FORMAT = "%d\$s"
    const val XLIFF_TAG_TEMPLATE = """<xliff:g id="%s" example="%s">%s</xliff:g>"""
    const val INVALID_CHARS_REGEX = "[^a-z0-9_]"
    const val REPLACEMENT_CHAR = "_"
}
