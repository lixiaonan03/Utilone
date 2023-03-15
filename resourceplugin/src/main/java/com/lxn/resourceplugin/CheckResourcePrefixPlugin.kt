package com.lxn.resourceplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.api.BaseVariantImpl
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lxn.resourceplugin.output.OutputResource
import com.lxn.resourceplugin.output.OutputResourceDetail
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jdom2.Element
import org.jdom2.input.SAXBuilder
import org.jdom2.located.LocatedElement
import org.jdom2.located.LocatedJDOMFactory
import java.io.File

class CheckResourcePrefixPlugin : Plugin<Project> {

    //资源集合
    private var mResourceMap = mutableMapOf<String, Resource>()

    //冲突的资源集合
    private var mConflictResourceMap = mutableMapOf<String, ArrayList<Resource>>()

    /**
     *  插件会执行到的
     */
    override fun apply(p0: Project) {


        // 这里只是随便定义一个Task而已，和Transform无关
        p0.tasks.register("lxn===JustTask") {
            println("单纯的测试task外层==${it.name}==${p0.name}====")
            it.doLast { inner ->
                println("单纯的测试task==${inner.name}==${p0.name}====")
            }
        }
        p0.tasks.create("lxnTask2") {
            println("单纯的测试task2==${p0.name}==${p0.plugins}=")
        }

        val isLibrary = p0.plugins.hasPlugin("com.android.library")
        val variants = if (isLibrary) {
            (p0.property("android") as LibraryExtension).libraryVariants
        } else {
            (p0.property("android") as AppExtension).applicationVariants
        }
        p0.afterEvaluate {
            variants.forEach { variant ->
                println("variant===${variant.name}")
                variant as BaseVariantImpl
                //任务名称
                val thisTaskName = "checkResource${variant.name.capitalize()}"
                //创建任务
                val thisTask = p0.task(thisTaskName)
                thisTask.group = "check"

                //开启任务
                val compileReleaseJavaWithJavac =
                    p0.tasks.findByName("compile${variant.name.capitalize()}JavaWithJavac")
                compileReleaseJavaWithJavac?.dependsOn(thisTask)

                thisTask.doLast {
                    val startTime = System.currentTimeMillis()
                    println("开始执行 CheckResource 任务：$thisTaskName")

                    //返回包含所有原始Android资源的文件集合，包括来自传递依赖项的资源
                    val files = variant.allRawAndroidResources.files

                    files.forEach { file ->
                        //遍历Set<File>，将value资源、file资源存进mResourceMap，发生冲突的资源则存进mConflictResourceMap
                        traverseResources(file)
                    }

                    // 打印出所有冲突的资源
                    val iterator = mConflictResourceMap.entries.iterator()
                    val fileResourceList = mutableListOf<OutputResource>()
                    val valueResourceList = mutableListOf<OutputResource>()

                    while (iterator.hasNext()) {

                        var isValueType = false
                        val entry = iterator.next()
                        println("冲突的点===${entry}")
                        val valueList = entry.value
                        val outputResource = OutputResource()
                        val outputResourceDetailList = mutableListOf<OutputResourceDetail>()
                        var uniqueId: String? = null
                        valueList.forEach {
                            uniqueId = it.uniqueId
                            val outputResourceDetail = OutputResourceDetail()
                            var resource: Resource? = null
                            if (it.isValueType) {
                                isValueType = true
                                resource = it as ValueResource
                                if (outputResource.title == null) {
                                    outputResource.title =
                                        resource.resName + " (数量：${valueList.size},)（id = $uniqueId )"
                                }
                            } else {
                                isValueType = false
                                resource = it as FileResource
                                if (outputResource.title == null) {
                                    outputResource.title =
                                        resource.fileName + " (数量：${valueList.size},)（id = $uniqueId )"
                                }
                            }
                            val modulePath = resource.belongFilePath()
                            var relatedFileName = modulePath
                            if (modulePath.contains("/")) {
                                relatedFileName =
                                    modulePath.substring(modulePath.lastIndexOf("/") + 1)
                            }
                            if (isValueType) {
                                relatedFileName =
                                    relatedFileName + "(Line: " + (it as ValueResource).getLine() + ")"
                            }
                            outputResourceDetail.title =
                                pretifyName(relatedFileName, 50) + "->" + modulePath
                            outputResourceDetailList.add(outputResourceDetail)
                        }
                        outputResource.children = outputResourceDetailList
                        if (isValueType) {
                            valueResourceList.add(outputResource)
                        } else {
                            fileResourceList.add(outputResource)
                        }


                    }

                    val cost = System.currentTimeMillis() - startTime
                    println("资源冲突检查完毕，耗时 " + cost + " ms")
                }


            }
        }

    }

    private fun pretifyName(content1: String, targetSize: Int): String {
        var content = content1
        val size = content.length
        if (size < targetSize) {
            content += " "
            for (index in 0 until (targetSize - size)) {
                content += "_"
            }
        }
        return content
    }


    /**
     *  遍历Set<File>，将value资源、file资源存进mResourceMap，发生冲突的资源则存进mConflictResourceMap
     */
    fun traverseResources(file: File) {
        if (file.isDirectory) {
            for (fileOne in file.listFiles()!!) {
                fileOne?.let {
                    traverseResources(fileOne)
                }
            }
        } else {
            //判断是值类型资源还是文件资源
            val isValueType = isValueResource(file)
            if (isValueType) {
                findAndRecordValueResource(file)
            } else {
                findAndRecordFileResource(file)
            }
        }
    }

    // 是否是值类型的资源
    private fun isValueResource(file: File): Boolean {
        if (file.parentFile.name == "values" || file.parentFile.name.startsWith("values-")) {
            return true
        }
        return false
    }

    private fun findAndRecordFileResource(file: File) {
        val resource = FileResource()
        resource.path = file.path
        resource.setLastDirectory(file.parentFile.name)
        resource.fileName = file.name
        resource.md5 = MD5Util.getMD5(file)
        recordResource(resource)
    }


    /**
     * 判断记录value 值资源的
     */
    private fun findAndRecordValueResource(file: File) {
        //获取资源id，
        //value资源id："value@" + lastDirectory + "/" + resName
        //file资源id："file@" + lastDirectory + "/" + fileName

        val lastDirectory = file.parentFile.name
        val filePath = file.path
        // 构造器
        val saxBuilder = SAXBuilder()
        saxBuilder.jdomFactory = LocatedJDOMFactory()
        // 获取文档
        val document = saxBuilder.build(file)
        // 得到根元素: resources
        val element = document.rootElement
        //解析xml 中的节点元素  通过name属性值获取元素
        // <string name="lxn_text">lxn-------English</string>
        if (element != null) {
            val children = element.children

            children.forEach {
                val resName = it.getAttributeValue("name")
                val resValue =
                    if (it.children?.size == 0) it.value else getValueFromElement(it.children)
                val resource = ValueResource()
                resource.resName = resName
                resource.resValue = resValue
                resource.lastDirectory = lastDirectory
                resource.filePath = filePath
                if (it is LocatedElement) {
                    resource.line = (it as LocatedElement).line
                }
                recordResource(resource)
            }
        }
    }


    private fun getValueFromElement(elementList: List<Element>): String {
        val jsonArray = JsonArray()
        elementList.forEach { it ->
            val jsonObject = JsonObject()
            jsonObject.addProperty("_name", it.name)
            val attributes = it.attributes
            val attributesJsonArray = JsonArray()
            attributes.forEach { attribute ->
                val attributeObj = JsonObject()
                attributeObj.addProperty("name", attribute.name)
                attributeObj.addProperty("value", attribute.value)
                attributesJsonArray.add(attributeObj)
            }
            jsonObject.add("_attributes", attributesJsonArray)
            jsonObject.addProperty("_value", it.value)
            jsonArray.add(jsonObject)
        }

        return jsonArray.toString()
    }


    /**
     * 记录资源的的
     */
    private fun recordResource(resource: Resource) {
        //获取资源id，
        //value资源id："value@" + lastDirectory + "/" + resName
        //file资源id："file@" + lastDirectory + "/" + fileName


        // 如果包含了，那么把相同资源方法一个Map中
        val uniqueId = resource.uniqueId
        if (mResourceMap.containsKey(uniqueId)) {
            val oldOne = mResourceMap[uniqueId]
            //如果真正需要 compare相同 ，需要 uniqueId 和 值一起相同
            if (oldOne != null && !oldOne.compare(resource)) {
                var resources = mConflictResourceMap[uniqueId]
                if (resources == null) {
                    resources = ArrayList()
                    resources.add(oldOne)
                }
                resources.add(resource)
                mConflictResourceMap[uniqueId] = resources

            }
        }
        mResourceMap[uniqueId] = resource
    }
}