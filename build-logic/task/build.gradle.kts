plugins {
    `kotlin-dsl`
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
}

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
    plugins {
        create("stringboard") {
            id = "task.plugin.notion.stringboard"
            implementationClass = "com.yunho.notion.NotionStringboardPlugin"
        }
    }
}
