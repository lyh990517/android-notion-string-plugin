plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
}

gradlePlugin {
    plugins {
        create("taskPlugin") {
            id = "com.yunho.notion.task"
            implementationClass = "com.yunho.notion.task.TaskPlugin"
        }
    }
}
