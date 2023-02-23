package com.lxn.exportplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ExportedPlugin : Plugin<Project> {
    override fun apply(p0: Project) {
       println("自定义的ExportedPlugin===开始$p0")
    }

}