package com.phenix.plugin.platform

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.xml.XmlDocument
import java.io.File
import java.io.FileNotFoundException

object AndroidUtils {


    /**
     * 获取android工程的包名
     * @param project Project
     */
    @Throws(FileNotFoundException::class, NullPointerException::class)
    fun getAppPackageName(project: Project): String? {
        val manifestFile = getManifestFile(project) ?: return null
        val xml = manifestFile.firstChild as? XmlDocument
        xml?.let {
            return xml.rootTag!!.getAttribute("package")!!.value
        }
        throw NullPointerException("File parsing exception")
    }


    /**
     * 获取 AndroidManifest.xml
     * @param project Project
     */
    fun getManifestFile(project: Project): PsiFile? {
        val path = project.basePath + File.separator +
                "app" + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "AndroidManifest.xml"
        val virtualFile = LocalFileSystem.getInstance().findFileByPath(path)
                ?: throw FileNotFoundException("Scanning $path file failed, the file was not detected!")
        return PsiManager.getInstance(project).findFile(virtualFile)
    }
}
