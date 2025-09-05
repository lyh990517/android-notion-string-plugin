package task.plugin.notion.model

sealed interface Language {
    val property: String
    val resDir: String

    data class Kor(override val property: String) : Language {
        override val resDir: String = "values-ko"
    }

    data class Jpn(override val property: String) : Language {
        override val resDir: String = "values-ja"
    }

    data class Eng(override val property: String) : Language {
        override val resDir: String = "values"
    }
}
