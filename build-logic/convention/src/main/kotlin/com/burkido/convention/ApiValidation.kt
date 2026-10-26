package com.burkido.convention

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import kotlinx.validation.api.dump
import kotlinx.validation.api.filterOutNonPublic
import kotlinx.validation.api.loadApiFromJvmClasses
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.jar.JarFile

/**
 * Keeps each library's public API in `api/<module>.api`, the same format the Kotlin
 * binary-compatibility-validator uses.
 *
 * Neither that plugin nor KGP's built-in ABI validation hooks into AGP 9's built-in Kotlin yet, so
 * this feeds the release classes into the validator's dump functions directly. Once KGP supports it,
 * this file can go.
 */
internal fun Project.configureApiValidation() {
    val apiFile = layout.projectDirectory.file("api/$name.api")
    val modulePath = path

    val apiDump = tasks.register<ApiDumpTask>("apiDump") {
        group = "verification"
        description = "Writes the public API of this library to api/$name.api."
        outputFile.set(apiFile)
    }
    val apiCheck = tasks.register<ApiCheckTask>("apiCheck") {
        group = "verification"
        description = "Fails when the public API no longer matches api/$name.api."
        expectedFile.from(apiFile)
        projectPath.set(modulePath)
    }
    tasks.named("check").configure { dependsOn(apiCheck) }

    extensions.configure<LibraryAndroidComponentsExtension> {
        onVariants(selector().withBuildType("release")) { variant ->
            val classes = variant.artifacts.forScope(ScopedArtifacts.Scope.PROJECT)
            classes.use(apiDump).toGet(ScopedArtifact.CLASSES, ApiDumpTask::jars, ApiDumpTask::dirs)
            classes.use(apiCheck).toGet(ScopedArtifact.CLASSES, ApiCheckTask::jars, ApiCheckTask::dirs)
        }
    }
}

abstract class ApiTask : DefaultTask() {

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val jars: ListProperty<RegularFile>

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val dirs: ListProperty<Directory>

    protected fun renderApi(): String {
        val classFiles = sequence {
            dirs.get().forEach { dir ->
                dir.asFile.walk()
                    .filter { it.isFile && isPublishedClass(it.name) }
                    .forEach { yield(it.readBytes()) }
            }
            jars.get().forEach { jar ->
                JarFile(jar.asFile).use { file ->
                    file.entries().asSequence()
                        .filter { isPublishedClass(it.name.substringAfterLast('/')) }
                        .forEach { entry -> yield(file.getInputStream(entry).use(InputStream::readBytes)) }
                }
            }
        }
        return classFiles
            .map(::ByteArrayInputStream)
            .loadApiFromJvmClasses()
            .filterOutNonPublic()
            .dump(StringBuilder())
            .toString()
    }

    // R and BuildConfig are generated per consumer build and aren't part of the library's API.
    private fun isPublishedClass(fileName: String): Boolean =
        fileName.endsWith(".class") &&
            fileName != "R.class" &&
            !fileName.startsWith("R$") &&
            fileName != "BuildConfig.class"
}

abstract class ApiDumpTask : ApiTask() {

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun dump() {
        outputFile.get().asFile.writeText(renderApi())
    }
}

abstract class ApiCheckTask : ApiTask() {

    // A file collection instead of a file property, so a missing dump gives a helpful error below.
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val expectedFile: ConfigurableFileCollection

    @get:Input
    abstract val projectPath: Property<String>

    @TaskAction
    fun check() {
        val expected = expectedFile.singleFile
        val fix = "Run ./gradlew ${projectPath.get()}:apiDump and commit ${expected.name} if the change is intended."
        if (!expected.exists()) throw GradleException("No API dump found at $expected. $fix")

        val expectedLines = expected.readText().normalizedLines()
        val actualLines = renderApi().normalizedLines()
        if (expectedLines == actualLines) return

        val removed = expectedLines - actualLines.toSet()
        val added = actualLines - expectedLines.toSet()
        val diff = (removed.map { "- $it" } + added.map { "+ $it" }).take(MAX_DIFF_LINES).joinToString("\n")
        throw GradleException("Public API of ${projectPath.get()} changed:\n$diff\n$fix")
    }

    private fun String.normalizedLines(): List<String> = replace("\r\n", "\n").trimEnd().lines()

    private companion object {
        const val MAX_DIFF_LINES = 40
    }
}
