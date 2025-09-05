plugins {
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "2.0.0"
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
}

group = "io.github.lyh990517"
version = "1.0.2"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    implementation(libs.jetbrains.kotlinx.serialization.json)
}

gradlePlugin {
    website = "https://github.com/lyh990517/notion-string-automation"
    vcsUrl = "https://github.com/lyh990517/notion-string-automation"

    plugins {
        create("stringboard") {
            id = "io.github.lyh990517.notion-stringboard"
            implementationClass = "task.plugin.notion.NotionStringboardPlugin"
            displayName = "Notion Stringboard Plugin"
            description = "A Gradle plugin that automatically generates Android string resources from Notion database with multi-language support"
            tags = listOf("notion", "android", "localization", "strings", "i18n", "automation")
        }
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                name.set("Notion Stringboard Plugin")
                description.set("A Gradle plugin that automatically generates Android string resources from Notion database")
                url.set("https://github.com/lyh990517/notion-string-automation")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("lyh990517")
                        name.set("lyh990517")
                        email.set("lyh990517@naver.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/lyh990517/notion-string-automation.git")
                    developerConnection.set("scm:git:ssh://github.com/lyh990517/notion-string-automation.git")
                    url.set("https://github.com/lyh990517/notion-string-automation")
                }
            }
        }
    }
}
