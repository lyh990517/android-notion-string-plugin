plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    kotlin("plugin.serialization") version "2.0.0"
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
}

gradlePlugin {
    plugins {
        create("stringboard") {
            id = "task.notion.stringboard"
            implementationClass = "com.yunho.notion.NotionStringboardPlugin"
        }
    }
}
