package com.badoo.ribs.plugin.action

import com.android.builder.model.AndroidProject
import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.badoo.ribs.plugin.generator.SourceSet
import com.badoo.ribs.plugin.generator.SourceSet.ANDROID_TEST
import com.badoo.ribs.plugin.generator.SourceSet.MAIN
import com.badoo.ribs.plugin.generator.SourceSet.RESOURCES
import com.badoo.ribs.plugin.generator.SourceSet.TEST
import com.badoo.ribs.plugin.util.toPsiDirectory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDirectory
import com.intellij.refactoring.PackageWrapper
import com.intellij.refactoring.util.RefactoringUtil
import org.jetbrains.android.facet.AndroidFacet

class SourceSetDirectoriesProvider(
    private val project: Project,
    private val androidFacet: AndroidFacet,
    private val targetPackage: PackageWrapper,
    private val mainSourceDirectory: PsiDirectory
) {

    private val androidModel = (AndroidModuleModel.get(androidFacet) as AndroidModuleModel)
    private val directoriesCache: MutableMap<SourceSet, PsiDirectory> = hashMapOf()

    fun getDirectory(sourceSet: SourceSet): PsiDirectory =
        directoriesCache[sourceSet] ?: findDirectoryOrCreate(sourceSet).also {
            directoriesCache[sourceSet] = it
        }

    private fun findDirectoryOrCreate(sourceSet: SourceSet): PsiDirectory = when (sourceSet) {
        MAIN -> mainSourceDirectory
        TEST -> getAndroidArtifactDirectory(AndroidProject.ARTIFACT_UNIT_TEST)
        ANDROID_TEST -> getAndroidArtifactDirectory(AndroidProject.ARTIFACT_ANDROID_TEST)
        RESOURCES -> androidFacet.allResourceDirectories.firstOrNull()?.toPsiDirectory(project)
            ?: throw IllegalStateException("Resources directory not found")
    }

    private fun getAndroidArtifactDirectory(artifact: String): PsiDirectory {
        val file = androidModel.getTestSourceProviders(artifact).firstOrNull()?.javaDirectories?.firstOrNull()
            ?: throw IllegalStateException("Source set directory for $artifact not found")
        file.mkdirs()
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
        return RefactoringUtil.createPackageDirectoryInSourceRoot(targetPackage, virtualFile)
    }
}
