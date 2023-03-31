package com.lxn.lxnplugin

import com.lxn.lxnplugin.Minify.Parameters
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import java.io.File

/**
 * 自定义一个新的TransformAction
 * @author：李晓楠
 * 时间：2023/3/24 14:24
 */
abstract class Minify : TransformAction<Parameters> {   // (1)
    interface Parameters : TransformParameters {               // (2)
        @get:Input
        var keepClassesByArtifact: Map<String, Set<String>>

    }

    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    override
    fun transform(outputs: TransformOutputs) {
        val fileName = inputArtifact.get().asFile.name
        for (entry in parameters.keepClassesByArtifact) {      // (3)
            if (fileName.startsWith(entry.key)) {
                val nameWithoutExtension = fileName.substring(0, fileName.length - 4)
                minify(inputArtifact.get().asFile, entry.value, outputs.file("${nameWithoutExtension}-min.jar"))
                return
            }
        }
        println("Nothing to minify - using $fileName unchanged")
        outputs.file(inputArtifact)                            // (4)
    }

    private fun minify(artifact: File, keepClasses: Set<String>, jarFile: File) {
        println("Minifying ${artifact.name}")
        // Implementation ...
    }
}