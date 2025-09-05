package task.plugin.notion.model

sealed interface Language {
    val property: String
    val resDir: String

    data class Arabic(override val property: String) : Language {
        override val resDir: String = "values-ar"
    }

    data class Bengali(override val property: String) : Language {
        override val resDir: String = "values-bn"
    }

    data class Bulgarian(override val property: String) : Language {
        override val resDir: String = "values-bg"
    }

    data class ChiSimplified(override val property: String) : Language {
        override val resDir: String = "values-zh-rCN"
    }

    data class ChiTraditional(override val property: String) : Language {
        override val resDir: String = "values-zh-rTW"
    }

    data class Croatian(override val property: String) : Language {
        override val resDir: String = "values-hr"
    }

    data class Czech(override val property: String) : Language {
        override val resDir: String = "values-cs"
    }

    data class Danish(override val property: String) : Language {
        override val resDir: String = "values-da"
    }

    data class Dutch(override val property: String) : Language {
        override val resDir: String = "values-nl"
    }

    data class English(override val property: String) : Language {
        override val resDir: String = "values"
    }

    data class Filipino(override val property: String) : Language {
        override val resDir: String = "values-fil"
    }

    data class Finnish(override val property: String) : Language {
        override val resDir: String = "values-fi"
    }

    data class French(override val property: String) : Language {
        override val resDir: String = "values-fr"
    }

    data class German(override val property: String) : Language {
        override val resDir: String = "values-de"
    }

    data class Greek(override val property: String) : Language {
        override val resDir: String = "values-el"
    }

    data class Gujarati(override val property: String) : Language {
        override val resDir: String = "values-gu"
    }

    data class Hebrew(override val property: String) : Language {
        override val resDir: String = "values-iw"
    }

    data class Hindi(override val property: String) : Language {
        override val resDir: String = "values-hi"
    }

    data class Hungarian(override val property: String) : Language {
        override val resDir: String = "values-hu"
    }

    data class Indonesian(override val property: String) : Language {
        override val resDir: String = "values-in"
    }

    data class Italian(override val property: String) : Language {
        override val resDir: String = "values-it"
    }

    data class Japanese(override val property: String) : Language {
        override val resDir: String = "values-ja"
    }

    data class Korean(override val property: String) : Language {
        override val resDir: String = "values-ko"
    }

    data class Malay(override val property: String) : Language {
        override val resDir: String = "values-ms"
    }

    data class Marathi(override val property: String) : Language {
        override val resDir: String = "values-mr"
    }

    data class Norwegian(override val property: String) : Language {
        override val resDir: String = "values-no"
    }

    data class Persian(override val property: String) : Language {
        override val resDir: String = "values-fa"
    }

    data class Polish(override val property: String) : Language {
        override val resDir: String = "values-pl"
    }

    data class Portuguese(override val property: String) : Language {
        override val resDir: String = "values-pt"
    }

    data class Punjabi(override val property: String) : Language {
        override val resDir: String = "values-pa"
    }

    data class Romanian(override val property: String) : Language {
        override val resDir: String = "values-ro"
    }

    data class Russian(override val property: String) : Language {
        override val resDir: String = "values-ru"
    }

    data class Serbian(override val property: String) : Language {
        override val resDir: String = "values-sr"
    }

    data class Spanish(override val property: String) : Language {
        override val resDir: String = "values-es"
    }

    data class Swahili(override val property: String) : Language {
        override val resDir: String = "values-sw"
    }

    data class Swedish(override val property: String) : Language {
        override val resDir: String = "values-sv"
    }

    data class Tamil(override val property: String) : Language {
        override val resDir: String = "values-ta"
    }

    data class Telugu(override val property: String) : Language {
        override val resDir: String = "values-te"
    }

    data class Thai(override val property: String) : Language {
        override val resDir: String = "values-th"
    }

    data class Turkish(override val property: String) : Language {
        override val resDir: String = "values-tr"
    }

    data class Ukrainian(override val property: String) : Language {
        override val resDir: String = "values-uk"
    }

    data class Urdu(override val property: String) : Language {
        override val resDir: String = "values-ur"
    }

    data class Vietnamese(override val property: String) : Language {
        override val resDir: String = "values-vi"
    }
}
