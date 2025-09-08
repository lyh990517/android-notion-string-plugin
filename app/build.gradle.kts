import task.plugin.notion.model.Language
import task.plugin.notion.query.NotionQueryBuilder
import task.plugin.notion.query.sort.Direction
import task.plugin.notion.query.sort.Timestamp
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("io.github.lyh990517.notion-stringboard") version "1.0.4"
}

android {
    namespace = "com.yunho.notion"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yunho.notion"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }
}

stringboard {
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }

    notionApiKey = localProperties.getProperty("NOTION_API_KEY")
    dataSourceId = localProperties.getProperty("DATA_SOURCE_ID")
    outputDir = "${project.rootDir}/app/src/main/res"
    queryBuilder = NotionQueryBuilder()
        .filter {
            multiSelect { "Part" doesNotContain "Server" } and
                    select { "Status" equals "InProgress" } and
                    richText { "String: BASE" contains "hello" } and
                    richText { "String: KOR" contains "안녕" } and
                    richText { "String: JPN" contains "こんにちは" } and
                    richText { "Resource ID" equals "id-1" } or
                    checkBox { "Deprecated" equals true }
        }.sort {
            property { "Resource ID" by Direction.DESCENDING }
            timestamp { Timestamp.CREATED_TIME by Direction.ASCENDING }
        }
    idPropertyName = "Resource ID"
    languages = listOf(
        Language.Korean("String: KOR"),
        Language.Japanese("String: JPN"),
        Language.English("String: BASE")
    )
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
