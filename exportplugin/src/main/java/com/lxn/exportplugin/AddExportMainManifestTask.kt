package com.lxn.exportplugin

import com.google.gson.Gson
import groovy.util.Node
import groovy.util.NodeList
import groovy.util.XmlParser
import groovy.xml.XmlUtil
import org.gradle.api.DefaultTask
import org.gradle.api.java.archives.ManifestException
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.com.google.gson.JsonSyntaxException
import java.io.File


/**
 * 在processDebugMainManifest之前对manifest进行处理
 */
open class AddExportMainManifestTask : DefaultTask() {
    //添加open 关键字让这个类可继承

    //mainManifest 的文件
    private lateinit var mainManifest: File

    //三方aar 文件
    private lateinit var manifests: Set<File>

    private val exportedErrorMessage =
        "清单合并失败：需要为 <activity> 明确指定 android:exported。 当相应组件定义了 Intent 过滤器时，面向 Android 12 及更高版本的应用需要为 android:exported 指定一个明确的值。 有关详细信息，请参阅 https://developer.android.com/guide/topics/manifest/activity-element#exported"
    private var exportedError = false

    //黑名单
    private var blackPackages = mutableListOf<String>()
    private var actionRules = mutableListOf("android.intent.action.MAIN")
    private var blackNames = mutableListOf<String>()
    private var blackIgnores = mutableListOf<String>()
    private var whiteNames = mutableListOf<String>()

    private var enableMainManifest = false
    private var outPutFile: File? = null

    private val qNameKey = "android:name"
    private val qExportedKey = "android:exported"

    /**
     * 设置三方aar的manifests 文案
     */
    fun setManifests(files: Set<File>) {
        this.manifests = files
    }

    /**
     * 设置主app的 mainManifest 的文件
     */
    fun setMainManifest(mainManifest: File) {
        this.mainManifest = mainManifest
    }

    /**
     * 设置额外的扩展配置
     */
    fun setExtArg(arg: ExportedExtension) {
        // 是否支持写入主清单文件,默认false,用户手动决定
        enableMainManifest = arg.enableMainManifest
        // 日志输出位置,会输出以下内容
        outPutFile = arg.outPutFile

        initRules(arg.ruleFile)
    }

    /**
     * 处理配置的规则的
     */
    private fun initRules(file: File?) {
        if (file == null) return
        val json = file.readText(Charsets.UTF_8)
        try {
            Gson().fromJson(json, RulesListBean::class.java)?.let {
                blackPackages.addAll(it.blackPackages)
                blackIgnores.addAll(it.blackIgnores)
                blackNames.addAll(it.blackNames)
                whiteNames.addAll(it.whiteNames)
                if (it.actionRules.isNotEmpty()) {
                    actionRules.clear()
                    actionRules.addAll(it.actionRules)
                }
            }
        } catch (t: JsonSyntaxException) {
            throw JsonSyntaxException("解析规则异常,请检查你的json格式是否正常,位置:[${file.path}]")
        }
    }


    @TaskAction
    fun action() {
        // 默认不对主manifest做处理,交给系统自行处理,主要原因是这里是我们业务可控制部分
        println("-----exported Task任务开始的->start------")
        val builder: StringBuilder = StringBuilder()
        builder.addRulesLog()
        exportedMain(builder)
        if (exportedError) {
            builder.append("## 处理终止,请手动处理主model。")
            writeOut(builder)
            throw ManifestException(exportedErrorMessage)
        }
        exportedAar(builder)
        writeOut(builder)
        println("-----exported->End-------")

    }

    /**
     * 处理主 exported 属性的配置
     */
    private fun exportedMain(builder: StringBuilder) {
        builder.append("## App-AndroidManifest\n")
        builder.append("> 这里是你的业务主model下需要调整的,建议手动处理。\n")
        exportedManifest(mainManifest, builder, true)
        builder.append("> 主model处理结束。\n")
        builder.append("---\n\n\n")
    }

    private fun exportedManifest(file: File, outBuilder: StringBuilder, isMain: Boolean) {
        val aarName = file.parentFile.name
        //如果不存在返回
        if (!file.exists()) return
        val xml = XmlParser(false, false).parse(file)
        val packageName = xml.attributes()["package"].toString()
        //获取appliction 节点下的数据
        val applicationNode = xml.nodeList().firstOrNull { it.name() == "application" } ?: return
        val nodes = applicationNode.children().asSequence().mapNotNull {
            it as? Node
        }.filter { it ->
            val name = it.name()
            (name == "activity" || name == "receiver" || name == "service") &&
                    it.nodeList().any { it.name() == "intent-filter" }

        }.toList().takeIf {
            // takeIf 表示复合条件就返回不复和就返回null
            it.isNotEmpty()
        }
        if (nodes === null) return
        outBuilder.append("#### 开始处理-> [$aarName]\n")
        outBuilder.append("- package: [$packageName]\n")
        outBuilder.append("- path: ${file.path}\n")
        var updateSum = 0
        val isBlackPackage = packageName.isBlackPackage
        nodes.forEachIndexed { _, node ->
            var exportedStateToBlack = false
            var exportedStateToWhite = false
            //是否更新
            val isUpdateSuccess =
                if (node.isWhite) {
                    if (node.isExported != true) {
                        node.isExported = true
                        exportedStateToWhite = true
                        true
                    } else false
                } else if (!node.isIgnore && (isBlackPackage || node.isBlack)) {
                    if (node.isExported != false) {
                        node.isExported = false
                        exportedStateToBlack = true
                        true
                    } else false
                } else node.updateExported()
            if (!isUpdateSuccess) return@forEachIndexed
            updateSum++
            outBuilder.addLog(updateSum, node, exportedStateToBlack, exportedStateToWhite)
        }
        val isWrite = updateSum > 0
        if (isWrite) outBuilder.append("- 处理结束,已处理 $updateSum 个\n\n")
        else {
            outBuilder.append("- 未匹配到符合规则的节点,处理结束\n\n")
            return
        }
        // 主main且禁止写入报错
        if (isMain && !enableMainManifest) {
            exportedError = true
            return
        }
        //最后回写文件
        val result = XmlUtil.serialize(xml)
        file.writer(Charsets.UTF_8).use {
            it.write(result)
        }
        return
    }

    private fun StringBuilder.addLog(pos: Int, node: Node, isBlack: Boolean, isWhite: Boolean) {
        val configure = if (isBlack) "黑名单" else if (isWhite) "白名单" else "无"
        append("  $pos. name:[${node.nodeName}],exported:[${node.isExported}],特殊配置:[$configure]\n")
    }


    private fun StringBuilder.addRulesLog() {
        append("# exported日志输出\n\n")
        append("## 当前插件配置\n")
        append("- enableMainManifest: [$enableMainManifest]\n")
        addRulesLog("- blackPackages\n", blackPackages)
        addRulesLog("- whiteNames\n", whiteNames)
        addRulesLog("- blackIgnores\n", blackIgnores)
        addRulesLog("- actionRules\n", actionRules)
        append("- logOutPath: [$outPutFile]\n\n")
    }

    private fun StringBuilder.addRulesLog(name: String, item: List<String>) {
        append(name)
        if (item.isEmpty()) {
            append("  - [null]\n")
            return
        }
        item.forEach {
            append("  - [$it]\n")
        }
    }

    private fun exportedAar(builder: StringBuilder) {
        builder.append("## aar-AndroidManifest\n")
        builder.append("> 这里是你的其他model或者aar下需要调整的,插件会自动进行处理。\n")
        manifests.forEach {
            exportedManifest(it, builder, false)
        }
    }



    private fun writeOut(outBuilder: StringBuilder) {
        outPutFile?.apply {
            createFileIfNoExists()
            writeText(outBuilder.toString())
        }
    }

    private fun File.createFileIfNoExists() {
        if (exists()) return
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        createNewFile()
    }

    private fun Node.nodeList() = (this.value() as NodeList).mapNotNull {
        // 用于防止某些不标准的写法,如//xx 注释直接写到了manifest里
        it as? Node
    }

    private val String.isBlackPackage
        get() = blackPackages.any { it == this }


    private val Node.nodeName: String?
        get() = attributes()["android:name"]?.toString()

    /**
     *  判断是否是白名单
     *    android:name=".module.main.ui.MainActivity"
     *    配置文件中的白名单配置必须如此 匹配才能对上
     */
    private val Node.isWhite: Boolean
        get() {
            val name = nodeName
            return whiteNames.any { name == it }
        }


    /**
     * 看这个 android:exported 属性是否存在
     */
    private var Node.isExported: Boolean?
        set(value) {
            attributes()[qExportedKey] = "$value"
        }
        get() = attributes()[qExportedKey]?.toString()?.toBoolean()

    /**
     * 判断是否忽略
     */
    private val Node.isIgnore: Boolean
        get() {
            val name = nodeName
            return blackIgnores.any { name == it }
        }


    private val Node.isBlack: Boolean
        get() {
            val name = nodeName
            return blackNames.any { name == it }
        }


    private fun Node.updateExported(): Boolean {
        if (attribute(qExportedKey) != null) return false
        val isExported = nodeList().any { node ->
            node.nodeList().any {
                it.name() == "action" && it.anyTag(qNameKey, actionRules)
            }
        }
        this.isExported = isExported
        return true
    }

    private fun Node.anyTag(key: String, values: List<String>): Boolean {
        // 如果规则为null,直接返回false,对于无法匹配的,做出扼制,不应让其显示声明出来
        if (values.isEmpty()) return false
        return attributes()[key]?.let { v ->
            val value = v.toString()
            values.any {
                it == value
            }
        } ?: false
    }
}