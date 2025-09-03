plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    kotlin("plugin.serialization") version "2.0.21"
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

gradlePlugin {
    plugins {
        create("taskPlugin") {
            id = "com.yunho.notion.task"
            implementationClass = "com.yunho.notion.task.TaskPlugin"
        }
    }
}
