package com.lxn.exportplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.tasks.ProcessApplicationManifest
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 *  @author 李晓楠
 *  功能描述: 自定义插件使用  练习export 支持使用
 *  时 间： 2023/2/24 10:48
 */
class ExportedPlugin : Plugin<Project> {

    companion object {
        private const val EXPORTED_EXT = "exported"
        private const val TASK_NAME = "ManifestExportedTask"
    }

    override fun apply(project: Project) {
        println("--通过kotlin--插件----自定义的ExportedPlugin===开始$project")
        if (!project.plugins.hasPlugin(AppPlugin::class.java)) {
            //如果不包含 app插件 则直接退出
            println("-不包含AppPlugin${project.plugins}")
            return
        }
        //获取build 文件中 额外的属性配置
        //exported {
        //    // 是否允许修改主main
        //    enableMainManifest true
        //    // 自定义日志输出位置,建议使用md格式
        //    // 默认输出位置,app/build/exported/outManifestLog.md
        //    outPutFile new File(("app/outManifestLog.md"))
        //    ruleFile new File("app/exported_black_list.json")
        //}
        println("-开始执行====${project.plugins}")
        project.extensions.create(EXPORTED_EXT, ExportedExtension::class.java)
        //添加task任务
        project.task(TASK_NAME)
        //解读配置的文件
        val ext = project.properties[EXPORTED_EXT] as ExportedExtension
        readAppModelVariant(project)
        project.afterEvaluate{
            if(ext.outPutFile == null){
                ext.outPutFile = File("${it.buildDir.absoluteFile.path}/exported/outManifestLog.md")
            }
            addMainManifestTask(ext,project)
        }


//        //检测每个task任务的执行时长的
//        var startTime = System.currentTimeMillis()
//        project.gradle.addListener(object :TaskExecutionListener{
//            override fun beforeExecute(p0: Task) {
//                //task开始执行之前开始记录时间
//                startTime = System.currentTimeMillis()
//            }
//
//            override fun afterExecute(p0: Task, p1: TaskState) {
//               val time = System.currentTimeMillis() - startTime
//                println("任务${p0.name} =执行的时间====${time}")
//            }
//
//        })
    }
    /** 添加task到processxxxMainManifest之前 如 processDebugMainManifest */
    private fun addMainManifestTask(ext: ExportedExtension,p: Project){
        variantNames.forEach{
            val t = p.tasks.getByName( String.format(
                "process%sMainManifest",
                it.capitalize()
            ))  as ProcessApplicationManifest
            val exportedTask = p.tasks.create("$it$TASK_NAME", AddExportMainManifestTask::class.java)
            exportedTask.setExtArg(ext)
            exportedTask.setMainManifest(t.mainManifest.get())
            exportedTask.setManifests(t.getManifests().files)
            t.dependsOn(exportedTask)
        }
    }



    private val variantNames = ArrayList<String>()

    /**
     *
     */
    private fun readAppModelVariant(p:Project){
        val appExtension = p.extensions.getByType(AppExtension::class.java)
        appExtension.variantFilter(
                Action {
                    variantNames.add(it.name)
                }
        )
    }
}