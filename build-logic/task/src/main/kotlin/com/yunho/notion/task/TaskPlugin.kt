package com.yunho.notion.task

import org.gradle.api.Plugin
import org.gradle.api.Project

class TaskPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("stringboardTask", StringboardTask::class.java) {
            group = "notion"
        }
    }
}
